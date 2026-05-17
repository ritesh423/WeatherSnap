package com.ritesh.weathersnap.ui.weather.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ritesh.weathersnap.domain.model.Weather
import com.ritesh.weathersnap.ui.theme.ChipAmber
import com.ritesh.weathersnap.ui.theme.ChipAmberText
import com.ritesh.weathersnap.ui.theme.ChipBlue
import com.ritesh.weathersnap.ui.theme.ChipBlueText
import com.ritesh.weathersnap.ui.theme.ChipTeal
import com.ritesh.weathersnap.ui.theme.ChipTealText
import com.ritesh.weathersnap.ui.theme.DeepGreen
import com.ritesh.weathersnap.ui.theme.Lime
import com.ritesh.weathersnap.ui.theme.OnSurface
import com.ritesh.weathersnap.ui.theme.OnSurfaceMuted
import com.ritesh.weathersnap.ui.theme.SurfaceCard
import com.ritesh.weathersnap.ui.theme.SurfaceCardSoft

@Composable
fun WeatherDetailsCard(
    weather: Weather,
    onCreateReport: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = weather.displayLocation,
                        style = MaterialTheme.typography.headlineSmall,
                        color = OnSurface
                    )
                    Text(
                        text = weather.condition,
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceMuted
                    )
                }
                Surface(
                    color = Lime,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = weather.temperatureLabel,
                        style = MaterialTheme.typography.titleLarge,
                        color = DeepGreen,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatChip(
                    label = "Humidity",
                    value = "${weather.humidityPercent}%",
                    background = ChipTeal,
                    valueColor = ChipTealText,
                    modifier = Modifier.weight(1f)
                )
                StatChip(
                    label = "Wind",
                    value = "%.2f m/s".format(weather.windSpeedMs),
                    background = ChipBlue,
                    valueColor = ChipBlueText,
                    modifier = Modifier.weight(1f)
                )
                StatChip(
                    label = "Pressure",
                    value = weather.pressureHpa.toInt().toString(),
                    background = ChipAmber,
                    valueColor = ChipAmberText,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(12.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SurfaceCardSoft,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Report readiness",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceMuted,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Camera and Room DB enabled",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurface
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onCreateReport,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Lime,
                    contentColor = DeepGreen
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Create Report")
            }
        }
    }
}
