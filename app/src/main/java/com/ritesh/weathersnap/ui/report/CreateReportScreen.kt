package com.ritesh.weathersnap.ui.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ritesh.weathersnap.ui.common.BackPill
import com.ritesh.weathersnap.ui.theme.OnSurfaceMuted
import com.ritesh.weathersnap.ui.theme.SurfaceDark
import com.ritesh.weathersnap.ui.weather.components.HeaderCard

@Composable
fun CreateReportScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
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
        Text(
            text = "Create Report screen coming soon.",
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurfaceMuted
        )
    }
}
