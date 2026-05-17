package com.ritesh.weathersnap.navigation

sealed class Routes(val route: String) {
    data object Weather : Routes("weather")
    data object CreateReport : Routes("create_report")
    data object Camera : Routes("camera")
    data object SavedReports : Routes("saved_reports")
}
