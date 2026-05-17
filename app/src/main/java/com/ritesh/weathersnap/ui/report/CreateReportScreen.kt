package com.ritesh.weathersnap.ui.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ritesh.weathersnap.navigation.CAPTURED_IMAGE_KEY
import com.ritesh.weathersnap.ui.common.BackPill
import com.ritesh.weathersnap.ui.theme.DeepGreen
import com.ritesh.weathersnap.ui.theme.ForestGreen
import com.ritesh.weathersnap.ui.theme.Lime
import com.ritesh.weathersnap.ui.theme.MintSoft
import com.ritesh.weathersnap.ui.theme.OnSurface
import com.ritesh.weathersnap.ui.theme.OnSurfaceMuted
import com.ritesh.weathersnap.ui.theme.SurfaceCard
import com.ritesh.weathersnap.ui.theme.SurfaceCardSoft
import com.ritesh.weathersnap.ui.theme.SurfaceDark
import com.ritesh.weathersnap.ui.weather.components.HeaderCard
import java.io.File

@Composable
fun CreateReportScreen(
    onBack: () -> Unit,
    onOpenCamera: () -> Unit,
    onSaved: () -> Unit,
    savedStateHandle: SavedStateHandle,
    modifier: Modifier = Modifier,
    viewModel: CreateReportViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val capturedImagePath by savedStateHandle
        .getStateFlow<String?>(CAPTURED_IMAGE_KEY, null)
        .collectAsStateWithLifecycle()

    LaunchedEffect(capturedImagePath) {
        capturedImagePath?.let {
            viewModel.onImageCaptured(it)
            savedStateHandle[CAPTURED_IMAGE_KEY] = null
        }
    }

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onSaved()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceDark)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderCard(
            title = "Create Report",
            subtitle = "Capture, compress, annotate",
            trailing = { BackPill(onClick = onBack) }
        )

        WeatherSummaryCard(weather = state.weather)

        ImageCard(
            imagePath = state.imagePath,
            isCompressing = state.isCompressing,
            onOpenCamera = onOpenCamera
        )

        if (state.imagePath != null) {
            SizeRow(
                original = state.originalSizeBytes,
                compressed = state.compressedSizeBytes
            )
        }

        NotesField(
            value = state.notes,
            onChange = viewModel::onNotesChange
        )

        state.errorMessage?.let { msg ->
            Text(
                text = msg,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFE57373)
            )
        }

        Button(
            onClick = viewModel::onSaveClick,
            enabled = state.canSave,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = Lime,
                contentColor = DeepGreen,
                disabledContainerColor = SurfaceCardSoft,
                disabledContentColor = OnSurfaceMuted
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(if (state.isSaving) "Saving..." else "Save Report")
        }
    }
}

@Composable
private fun WeatherSummaryCard(weather: com.ritesh.weathersnap.domain.model.Weather) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = weather.displayLocation,
                    style = MaterialTheme.typography.titleMedium,
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
                    style = MaterialTheme.typography.titleMedium,
                    color = DeepGreen,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun ImageCard(
    imagePath: String?,
    isCompressing: Boolean,
    onOpenCamera: () -> Unit
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceCardSoft),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isCompressing -> CircularProgressIndicator(color = Lime)
                    imagePath != null -> AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(File(imagePath))
                            .build(),
                        contentDescription = "Captured photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    else -> Text(
                        text = "No photo captured yet",
                        color = OnSurfaceMuted,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(Modifier.size(12.dp))
            Button(
                onClick = onOpenCamera,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ForestGreen,
                    contentColor = MintSoft
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (imagePath == null) "Open Camera" else "Retake")
            }
        }
    }
}

@Composable
private fun SizeRow(original: Long, compressed: Long) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = SurfaceCardSoft,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Original ${formatBytes(original)}",
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurfaceMuted,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "→",
                color = OnSurfaceMuted,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.size(8.dp))
            Text(
                text = "Compressed ${formatBytes(compressed)}",
                style = MaterialTheme.typography.bodyMedium,
                color = Lime
            )
        }
    }
}

@Composable
private fun NotesField(value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        placeholder = { Text("Add notes about this report", color = OnSurfaceMuted) },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = OnSurface,
            unfocusedTextColor = OnSurface,
            focusedContainerColor = SurfaceCard,
            unfocusedContainerColor = SurfaceCard,
            focusedBorderColor = Lime,
            unfocusedBorderColor = SurfaceCardSoft,
            cursorColor = Lime
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
    )
}

private fun formatBytes(bytes: Long): String {
    if (bytes <= 0) return "0 KB"
    val kb = bytes / 1024.0
    return if (kb >= 1024) "%.1f MB".format(kb / 1024.0)
    else "%.0f KB".format(kb)
}
