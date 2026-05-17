package com.ritesh.weathersnap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ritesh.weathersnap.ui.theme.WeatherSnapTheme
import com.ritesh.weathersnap.ui.weather.WeatherScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherSnapTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    Surface(modifier = Modifier.fillMaxSize()) {
                        WeatherScreen(
                            modifier = Modifier.padding(padding),
                            onCreateReport = {},
                            onReportsClick = {}
                        )
                    }
                }
            }
        }
    }
}
