package com.ritesh.weathersnap.ui.weather

import com.ritesh.weathersnap.domain.model.Weather

sealed interface WeatherUiState {
    data object Idle : WeatherUiState
    data object Loading : WeatherUiState
    data class Success(val weather: Weather) : WeatherUiState
    data object Empty : WeatherUiState
    data class Error(val message: String) : WeatherUiState
}
