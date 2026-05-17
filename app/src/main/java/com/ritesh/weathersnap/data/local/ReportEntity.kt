package com.ritesh.weathersnap.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cityName: String,
    val country: String?,
    val condition: String,
    val temperatureC: Double,
    val humidityPercent: Int,
    val windSpeedMs: Double,
    val pressureHpa: Double,
    val notes: String = "",
    val imagePath: String? = null,
    val originalSizeBytes: Long = 0,
    val compressedSizeBytes: Long = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val status: String = STATUS_DRAFT
) {
    companion object {
        const val STATUS_DRAFT = "DRAFT"
        const val STATUS_SAVED = "SAVED"
    }
}
