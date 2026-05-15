package com.ritesh.weathersnap.domain.model

data class Weather(
    val cityName: String,
    val country: String?,
    val temperatureC: Double,
    val condition: String,
    val humidityPercent: Int,
    val windSpeedMs: Double,
    val pressureHpa: Double
) {
    val displayLocation: String
        get() = country?.let { "$cityName, $it" } ?: cityName

    val temperatureLabel: String
        get() = "${temperatureC.toInt()}°C"
}
