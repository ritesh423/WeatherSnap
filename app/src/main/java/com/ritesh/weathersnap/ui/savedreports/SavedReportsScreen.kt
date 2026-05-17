package com.ritesh.weathersnap.ui.savedreports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.ritesh.weathersnap.ui.common.BackPill
import com.ritesh.weathersnap.ui.theme.DeepGreen
import com.ritesh.weathersnap.ui.theme.ForestGreen
import com.ritesh.weathersnap.ui.theme.OnSurface
import com.ritesh.weathersnap.ui.theme.OnSurfaceMuted
import com.ritesh.weathersnap.ui.theme.SurfaceCard
import com.ritesh.weathersnap.ui.theme.SurfaceDark
import com.ritesh.weathersnap.ui.weather.components.HeaderCard

@Composable
fun SavedReportsScreen(
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
            title = "Saved Reports",
            subtitle = "0 reports stored locally",
            trailing = { BackPill(onClick = onBack) }
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    DeepGreen.copy(alpha = 0.55f),
                                    ForestGreen.copy(alpha = 0.65f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No reports yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = OnSurface
                    )
                }
                Spacer(Modifier.height(14.dp))
                Text(
                    text = "Create and save a weather report to see image, notes, and weather details here.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceMuted
                )
            }
        }
    }
}
