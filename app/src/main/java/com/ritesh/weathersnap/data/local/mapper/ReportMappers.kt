package com.ritesh.weathersnap.data.local.mapper

import com.ritesh.weathersnap.data.local.ReportEntity
import com.ritesh.weathersnap.domain.model.Report
import com.ritesh.weathersnap.domain.model.Weather

fun ReportEntity.toDomain(): Report = Report(
    id = id,
    weather = Weather(
        cityName = cityName,
        country = country,
        temperatureC = temperatureC,
        condition = condition,
        humidityPercent = humidityPercent,
        windSpeedMs = windSpeedMs,
        pressureHpa = pressureHpa
    ),
    notes = notes,
    imagePath = imagePath,
    originalSizeBytes = originalSizeBytes,
    compressedSizeBytes = compressedSizeBytes,
    createdAt = createdAt
)

fun Weather.toEntity(
    notes: String = "",
    imagePath: String? = null,
    originalSizeBytes: Long = 0,
    compressedSizeBytes: Long = 0,
    status: String = ReportEntity.STATUS_DRAFT
): ReportEntity = ReportEntity(
    cityName = cityName,
    country = country,
    condition = condition,
    temperatureC = temperatureC,
    humidityPercent = humidityPercent,
    windSpeedMs = windSpeedMs,
    pressureHpa = pressureHpa,
    notes = notes,
    imagePath = imagePath,
    originalSizeBytes = originalSizeBytes,
    compressedSizeBytes = compressedSizeBytes,
    status = status
)
