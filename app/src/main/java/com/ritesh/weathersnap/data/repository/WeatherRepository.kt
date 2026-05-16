package com.ritesh.weathersnap.data.repository

import androidx.collection.LruCache
import com.ritesh.weathersnap.data.remote.OpenMeteoApi
import com.ritesh.weathersnap.data.remote.mapper.toDomain
import com.ritesh.weathersnap.domain.model.City
import com.ritesh.weathersnap.domain.model.Weather
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val api: OpenMeteoApi
) {

    private val suggestionCache = LruCache<String, List<City>>(50)

    suspend fun searchCities(query: String): Result<List<City>> {
        val key = query.trim().lowercase()
        if (key.length <= 2) return Result.success(emptyList())

        suggestionCache.get(key)?.let { return Result.success(it) }

        return runCatching {
            val response = api.searchCities(key)
            val cities = response.results.orEmpty().map { it.toDomain() }
            suggestionCache.put(key, cities)
            cities
        }
    }

    suspend fun getWeather(city: City): Result<Weather> = runCatching {
        api.getCurrentWeather(
            latitude = city.latitude,
            longitude = city.longitude
        ).toDomain(city)
    }
}
