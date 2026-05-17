package com.ritesh.weathersnap.domain.model

data class Report(
    val id: Long,
    val weather: Weather,
    val notes: String,
    val imagePath: String?,
    val originalSizeBytes: Long,
    val compressedSizeBytes: Long,
    val createdAt: Long
)
