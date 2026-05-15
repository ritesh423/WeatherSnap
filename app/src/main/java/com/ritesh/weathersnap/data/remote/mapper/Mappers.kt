package com.ritesh.weathersnap.data.remote.mapper

import com.ritesh.weathersnap.data.remote.dto.CityDto
import com.ritesh.weathersnap.data.remote.dto.ForecastResponse
import com.ritesh.weathersnap.domain.model.City
import com.ritesh.weathersnap.domain.model.Weather

fun CityDto.toDomain(): City = City(
    name = name,
    country = country,
    latitude = latitude,
    longitude = longitude
)

fun ForecastResponse.toDomain(city: City): Weather = Weather(
    cityName = city.name,
    country = city.country,
    temperatureC = current.temperature,
    condition = WeatherCodeMapper.describe(current.weatherCode),
    humidityPercent = current.humidity,
    windSpeedMs = current.windSpeed,
    pressureHpa = current.pressure
)
