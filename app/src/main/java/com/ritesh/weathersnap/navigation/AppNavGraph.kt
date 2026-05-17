package com.ritesh.weathersnap.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.ritesh.weathersnap.ui.camera.CameraScreen
import com.ritesh.weathersnap.ui.report.CreateReportScreen
import com.ritesh.weathersnap.ui.savedreports.SavedReportsScreen
import com.ritesh.weathersnap.ui.weather.WeatherScreen

const val CAPTURED_IMAGE_KEY = "captured_image"

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val gson = remember { Gson() }
    NavHost(
        navController = navController,
        startDestination = Routes.Weather.route,
        modifier = modifier
    ) {
        composable(Routes.Weather.route) {
            WeatherScreen(
                onCreateReport = { weather ->
                    val json = gson.toJson(weather)
                    navController.navigate(Routes.CreateReport.build(json))
                },
                onReportsClick = { navController.navigate(Routes.SavedReports.route) }
            )
        }
        composable(
            route = Routes.CreateReport.route,
            arguments = listOf(navArgument(Routes.CreateReport.ARG_WEATHER_JSON) {
                type = NavType.StringType
            })
        ) { entry ->
            CreateReportScreen(
                onBack = { navController.popBackStack() },
                onOpenCamera = { navController.navigate(Routes.Camera.route) },
                savedStateHandle = entry.savedStateHandle,
                onSaved = {
                    navController.popBackStack(Routes.Weather.route, inclusive = false)
                }
            )
        }
        composable(Routes.Camera.route) {
            CameraScreen(
                onClose = { navController.popBackStack() },
                onImageCaptured = { path ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(CAPTURED_IMAGE_KEY, path)
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.SavedReports.route) {
            SavedReportsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
