package com.ritesh.weathersnap.ui.report

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ritesh.weathersnap.data.image.ImageCompressor
import com.ritesh.weathersnap.data.local.ReportEntity
import com.ritesh.weathersnap.data.local.mapper.toEntity
import com.ritesh.weathersnap.data.repository.ReportRepository
import com.ritesh.weathersnap.domain.model.Weather
import com.ritesh.weathersnap.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateReportViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val reportRepository: ReportRepository,
    private val imageCompressor: ImageCompressor,
    private val gson: Gson
) : ViewModel() {

    private val weather: Weather = run {
        val json = savedStateHandle.get<String>(Routes.CreateReport.ARG_WEATHER_JSON)
            ?: error("Missing weather argument")
        gson.fromJson(json, Weather::class.java)
    }

    private val _state = MutableStateFlow(CreateReportUiState(weather = weather))
    val state: StateFlow<CreateReportUiState> = _state.asStateFlow()

    fun onNotesChange(text: String) {
        _state.update { it.copy(notes = text) }
    }

    fun onImageCaptured(rawPath: String) {
        if (_state.value.isCompressing) return
        viewModelScope.launch {
            _state.update { it.copy(isCompressing = true, errorMessage = null) }
            runCatching { imageCompressor.compress(rawPath) }
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isCompressing = false,
                            imagePath = result.compressedPath,
                            originalSizeBytes = result.originalSizeBytes,
                            compressedSizeBytes = result.compressedSizeBytes
                        )
                    }
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            isCompressing = false,
                            errorMessage = e.message ?: "Image compression failed"
                        )
                    }
                }
        }
    }

    fun onSaveClick() {
        val current = _state.value
        if (!current.canSave) return
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, errorMessage = null) }
            runCatching {
                val entity = current.weather.toEntity(
                    notes = current.notes.trim(),
                    imagePath = current.imagePath,
                    originalSizeBytes = current.originalSizeBytes,
                    compressedSizeBytes = current.compressedSizeBytes,
                    status = ReportEntity.STATUS_SAVED
                )
                reportRepository.insert(entity)
            }
                .onSuccess { _state.update { it.copy(isSaving = false, isSaved = true) } }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            isSaving = false,
                            errorMessage = e.message ?: "Failed to save report"
                        )
                    }
                }
        }
    }
}
