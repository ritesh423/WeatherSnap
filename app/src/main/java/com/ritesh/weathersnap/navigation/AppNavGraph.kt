package com.ritesh.weathersnap.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ritesh.weathersnap.ui.camera.CameraScreen
import com.ritesh.weathersnap.ui.report.CreateReportScreen
import com.ritesh.weathersnap.ui.savedreports.SavedReportsScreen
import com.ritesh.weathersnap.ui.weather.WeatherScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Weather.route,
        modifier = modifier
    ) {
        composable(Routes.Weather.route) {
            WeatherScreen(
                onCreateReport = { navController.navigate(Routes.CreateReport.route) },
                onReportsClick = { navController.navigate(Routes.SavedReports.route) }
            )
        }
        composable(Routes.CreateReport.route) {
            CreateReportScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.Camera.route) {
            CameraScreen(
                onClose = { navController.popBackStack() }
            )
        }
        composable(Routes.SavedReports.route) {
            SavedReportsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
