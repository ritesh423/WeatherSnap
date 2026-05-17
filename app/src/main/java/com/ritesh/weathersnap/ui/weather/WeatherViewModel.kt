package com.ritesh.weathersnap.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ritesh.weathersnap.data.repository.WeatherRepository
import com.ritesh.weathersnap.domain.model.City
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _searchTrigger = MutableStateFlow("")

    private val _suggestions = MutableStateFlow<List<City>>(emptyList())
    val suggestions: StateFlow<List<City>> = _suggestions.asStateFlow()

    private val _state = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val state: StateFlow<WeatherUiState> = _state.asStateFlow()

    init {
        observeSearchTrigger()
    }

    fun onQueryChange(text: String) {
        _inputText.value = text
        _searchTrigger.value = text
    }

    fun onCitySelected(city: City) {
        _inputText.value = city.displayName
        _suggestions.value = emptyList()
        viewModelScope.launch {
            _state.value = WeatherUiState.Loading
            repository.getWeather(city)
                .onSuccess { weather -> _state.value = WeatherUiState.Success(weather) }
                .onFailure { e ->
                    _state.value = WeatherUiState.Error(e.message ?: "Failed to load weather")
                }
        }
    }

    private fun observeSearchTrigger() {
        viewModelScope.launch {
            _searchTrigger
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    _suggestions.value = repository.searchCities(query).getOrDefault(emptyList())
                }
        }
    }
}
