package com.ritesh.weathersnap.ui.weather.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ritesh.weathersnap.domain.model.City
import com.ritesh.weathersnap.ui.theme.OnSurface
import com.ritesh.weathersnap.ui.theme.SurfaceCard
import com.ritesh.weathersnap.ui.theme.SurfaceCardSoft

@Composable
fun SuggestionsCard(
    suggestions: List<City>,
    onPick: (City) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            suggestions.forEach { city ->
                Surface(
                    onClick = { onPick(city) },
                    shape = RoundedCornerShape(50),
                    color = SurfaceCardSoft,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = city.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = OnSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp)
                    )
                }
            }
        }
    }
}
