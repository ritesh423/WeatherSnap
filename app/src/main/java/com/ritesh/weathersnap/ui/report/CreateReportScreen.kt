package com.ritesh.weathersnap.ui.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ritesh.weathersnap.navigation.CAPTURED_IMAGE_KEY
import com.ritesh.weathersnap.ui.common.BackPill
import com.ritesh.weathersnap.ui.theme.DeepGreen
import com.ritesh.weathersnap.ui.theme.Lime
import com.ritesh.weathersnap.ui.theme.OnSurfaceMuted
import com.ritesh.weathersnap.ui.theme.SurfaceDark
import com.ritesh.weathersnap.ui.weather.components.HeaderCard

@Composable
fun CreateReportScreen(
    onBack: () -> Unit,
    onOpenCamera: () -> Unit,
    savedStateHandle: SavedStateHandle?,
    modifier: Modifier = Modifier
) {
    val capturedImagePath by (savedStateHandle
        ?.getStateFlow<String?>(CAPTURED_IMAGE_KEY, null)
        ?.collectAsStateWithLifecycle()
        ?: return)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceDark)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderCard(
            title = "Create Report",
            subtitle = "Capture, compress, annotate",
            trailing = { BackPill(onClick = onBack) }
        )

        Button(
            onClick = onOpenCamera,
            colors = ButtonDefaults.buttonColors(
                containerColor = Lime,
                contentColor = DeepGreen
            )
        ) {
            Text(text = "Open Camera")
        }

        Text(
            text = capturedImagePath?.let { "Captured: $it" } ?: "No image captured yet.",
            style = MaterialTheme.typography.bodyMedium,
            color = capturedImagePath?.let { Color.White } ?: OnSurfaceMuted
        )
    }
}
