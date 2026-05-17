package com.ritesh.weathersnap.ui.report

import com.ritesh.weathersnap.domain.model.Weather

data class CreateReportUiState(
    val weather: Weather,
    val notes: String = "",
    val imagePath: String? = null,
    val originalSizeBytes: Long = 0,
    val compressedSizeBytes: Long = 0,
    val isCompressing: Boolean = false,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
) {
    val canSave: Boolean
        get() = imagePath != null && !isCompressing && !isSaving && !isSaved
}
