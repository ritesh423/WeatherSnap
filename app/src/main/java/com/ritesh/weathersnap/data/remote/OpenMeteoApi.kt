package com.ritesh.weathersnap.data.remote

import com.ritesh.weathersnap.data.remote.dto.ForecastResponse
import com.ritesh.weathersnap.data.remote.dto.GeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoApi {

    @GET("https://geocoding-api.open-meteo.com/v1/search")
    suspend fun searchCities(
        @Query("name") name: String,
        @Query("count") count: Int = 10,
        @Query("language") language: String = "en",
        @Query("format") format: String = "json"
    ): GeocodingResponse

    @GET("https://api.open-meteo.com/v1/forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = CURRENT_FIELDS,
        @Query("wind_speed_unit") windSpeedUnit: String = "ms"
    ): ForecastResponse

    companion object {
        private const val CURRENT_FIELDS =
            "temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m,surface_pressure"
    }
}
