package com.ritesh.weathersnap.ui.weather.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ritesh.weathersnap.ui.theme.DeepGreen
import com.ritesh.weathersnap.ui.theme.Lime
import com.ritesh.weathersnap.ui.theme.OnSurface
import com.ritesh.weathersnap.ui.theme.OnSurfaceMuted
import com.ritesh.weathersnap.ui.theme.SurfaceCard

@Composable
fun SearchCard(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    label = { Text("City") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f),
                    trailingIcon = {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Lime,
                                strokeWidth = 2.dp,
                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .size(18.dp)
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Lime,
                        unfocusedBorderColor = OnSurfaceMuted.copy(alpha = 0.4f),
                        focusedLabelColor = Lime,
                        unfocusedLabelColor = OnSurfaceMuted,
                        cursorColor = Lime,
                        focusedTextColor = OnSurface,
                        unfocusedTextColor = OnSurface
                    )
                )
                Spacer(Modifier.width(12.dp))
                Button(
                    onClick = onSearchClick,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Lime,
                        contentColor = DeepGreen
                    ),
                    modifier = Modifier.height(56.dp)
                ) {
                    Text("Search")
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Enter more than 2 letters to start city suggestions.",
                style = MaterialTheme.typography.bodySmall,
                color = OnSurfaceMuted
            )
        }
    }
}
