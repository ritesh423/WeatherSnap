package com.ritesh.weathersnap.ui.savedreports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ritesh.weathersnap.domain.model.Report
import com.ritesh.weathersnap.ui.common.BackPill
import com.ritesh.weathersnap.ui.theme.DeepGreen
import com.ritesh.weathersnap.ui.theme.ForestGreen
import com.ritesh.weathersnap.ui.theme.Lime
import com.ritesh.weathersnap.ui.theme.OnSurface
import com.ritesh.weathersnap.ui.theme.OnSurfaceMuted
import com.ritesh.weathersnap.ui.theme.SurfaceCard
import com.ritesh.weathersnap.ui.theme.SurfaceCardSoft
import com.ritesh.weathersnap.ui.theme.SurfaceDark
import com.ritesh.weathersnap.ui.weather.components.HeaderCard
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SavedReportsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SavedReportsViewModel = hiltViewModel()
) {
    val reports by viewModel.reports.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceDark)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderCard(
            title = "Saved Reports",
            subtitle = "${reports.size} report${if (reports.size == 1) "" else "s"} stored locally",
            trailing = { BackPill(onClick = onBack) }
        )

        if (reports.isEmpty()) {
            EmptyState()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = reports, key = { it.id }) { report ->
                    ReportCard(report = report)
                }
            }
        }
    }
}

@Composable
private fun EmptyState() {
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

@Composable
private fun ReportCard(report: Report) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(SurfaceCardSoft),
                    contentAlignment = Alignment.Center
                ) {
                    if (report.imagePath != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(File(report.imagePath))
                                .build(),
                            contentDescription = "Report photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Text("No photo", color = OnSurfaceMuted, style = MaterialTheme.typography.labelSmall)
                    }
                }
                Spacer(Modifier.size(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = report.weather.displayLocation,
                        style = MaterialTheme.typography.titleMedium,
                        color = OnSurface
                    )
                    Text(
                        text = "${report.weather.condition} • ${formatTimestamp(report.createdAt)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = OnSurfaceMuted
                    )
                }
                Surface(
                    color = Lime,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = report.weather.temperatureLabel,
                        style = MaterialTheme.typography.titleMedium,
                        color = DeepGreen,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            if (report.notes.isNotBlank()) {
                Spacer(Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceCardSoft,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = report.notes,
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurface,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                MetaChip(label = "Humidity", value = "${report.weather.humidityPercent}%", modifier = Modifier.weight(1f))
                MetaChip(label = "Wind", value = "%.1f m/s".format(report.weather.windSpeedMs), modifier = Modifier.weight(1f))
                MetaChip(label = "Pressure", value = report.weather.pressureHpa.toInt().toString(), modifier = Modifier.weight(1f))
            }

            if (report.originalSizeBytes > 0 || report.compressedSizeBytes > 0) {
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "Original ${formatBytes(report.originalSizeBytes)} → Compressed ${formatBytes(report.compressedSizeBytes)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurfaceMuted
                )
            }
        }
    }
}

@Composable
private fun MetaChip(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = SurfaceCardSoft,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = OnSurfaceMuted)
            Text(value, style = MaterialTheme.typography.bodyMedium, color = OnSurface)
        }
    }
}

private fun formatTimestamp(epochMs: Long): String {
    val sdf = SimpleDateFormat("MMM d, HH:mm", Locale.getDefault())
    return sdf.format(Date(epochMs))
}

private fun formatBytes(bytes: Long): String {
    if (bytes <= 0) return "0 KB"
    val kb = bytes / 1024.0
    return if (kb >= 1024) "%.1f MB".format(kb / 1024.0)
    else "%.0f KB".format(kb)
}
