package com.ritesh.weathersnap.ui.report

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ritesh.weathersnap.data.image.CompressionResult
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
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CreateReportViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val reportRepository: ReportRepository,
    private val imageCompressor: ImageCompressor,
    gson: Gson
) : ViewModel() {

    private val weather: Weather = run {
        val json = savedStateHandle.get<String>(Routes.CreateReport.ARG_WEATHER_JSON)
            ?: error("Missing weather argument")
        gson.fromJson(json, Weather::class.java)
    }

    private val _state = MutableStateFlow(
        CreateReportUiState(
            weather = weather,
            notes = savedStateHandle.get<String>(KEY_NOTES).orEmpty()
        )
    )
    val state: StateFlow<CreateReportUiState> = _state.asStateFlow()

    init {
        restoreDraftIfAny()
    }

    fun onNotesChange(text: String) {
        savedStateHandle[KEY_NOTES] = text
        _state.update { it.copy(notes = text) }
    }

    fun onImageCaptured(rawPath: String) {
        if (_state.value.isCompressing) return
        viewModelScope.launch {
            _state.update { it.copy(isCompressing = true, errorMessage = null) }
            runCatching { imageCompressor.compress(rawPath) }
                .onSuccess { result ->
                    persistDraft(result)
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
        val draftId = savedStateHandle.get<Long>(KEY_DRAFT_ID) ?: return
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, errorMessage = null) }
            runCatching {
                val existing = reportRepository.getById(draftId)
                    ?: error("Draft not found")
                val finalized = existing.copy(
                    notes = current.notes.trim(),
                    status = ReportEntity.STATUS_SAVED
                )
                reportRepository.update(finalized)
            }
                .onSuccess {
                    savedStateHandle.remove<String>(KEY_NOTES)
                    savedStateHandle.remove<Long>(KEY_DRAFT_ID)
                    _state.update { it.copy(isSaving = false, isSaved = true) }
                }
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

    private fun restoreDraftIfAny() {
        val draftId = savedStateHandle.get<Long>(KEY_DRAFT_ID) ?: return
        viewModelScope.launch {
            val entity = reportRepository.getById(draftId)
            when {
                entity == null -> savedStateHandle.remove<Long>(KEY_DRAFT_ID)
                entity.status == ReportEntity.STATUS_SAVED ->
                    _state.update { it.copy(isSaved = true) }
                else -> _state.update {
                    it.copy(
                        imagePath = entity.imagePath,
                        originalSizeBytes = entity.originalSizeBytes,
                        compressedSizeBytes = entity.compressedSizeBytes
                    )
                }
            }
        }
    }

    private suspend fun persistDraft(result: CompressionResult) {
        val existingId = savedStateHandle.get<Long>(KEY_DRAFT_ID)
        val existing = existingId?.let { reportRepository.getById(it) }

        if (existing != null && existing.status == ReportEntity.STATUS_DRAFT) {
            val previousImage = existing.imagePath
            val updated = existing.copy(
                imagePath = result.compressedPath,
                originalSizeBytes = result.originalSizeBytes,
                compressedSizeBytes = result.compressedSizeBytes
            )
            reportRepository.update(updated)
            if (previousImage != null && previousImage != result.compressedPath) {
                runCatching { File(previousImage).delete() }
            }
        } else {
            reportRepository.deleteAllDrafts()
            val entity = weather.toEntity(
                notes = _state.value.notes.trim(),
                imagePath = result.compressedPath,
                originalSizeBytes = result.originalSizeBytes,
                compressedSizeBytes = result.compressedSizeBytes,
                status = ReportEntity.STATUS_DRAFT
            )
            val newId = reportRepository.insert(entity)
            savedStateHandle[KEY_DRAFT_ID] = newId
        }
    }

    companion object {
        private const val KEY_NOTES = "create_report_notes"
        private const val KEY_DRAFT_ID = "create_report_draft_id"
    }
}
