package com.ritesh.weathersnap.navigation

import android.net.Uri

sealed class Routes(val route: String) {
    data object Weather : Routes("weather")
    data object CreateReport : Routes("create_report/{weatherJson}") {
        const val ARG_WEATHER_JSON = "weatherJson"
        fun build(weatherJson: String) = "create_report/${Uri.encode(weatherJson)}"
    }
    data object Camera : Routes("camera")
    data object SavedReports : Routes("saved_reports")
}
