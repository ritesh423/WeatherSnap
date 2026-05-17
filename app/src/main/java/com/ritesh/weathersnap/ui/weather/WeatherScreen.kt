package com.ritesh.weathersnap.ui.weather

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ritesh.weathersnap.domain.model.Weather
import com.ritesh.weathersnap.ui.theme.ForestGreen
import com.ritesh.weathersnap.ui.theme.Lime
import com.ritesh.weathersnap.ui.theme.MintSoft
import com.ritesh.weathersnap.ui.theme.SurfaceDark
import com.ritesh.weathersnap.ui.weather.components.ErrorCard
import com.ritesh.weathersnap.ui.weather.components.HeaderCard
import com.ritesh.weathersnap.ui.weather.components.SearchCard
import com.ritesh.weathersnap.ui.weather.components.SuggestionsCard
import com.ritesh.weathersnap.ui.weather.components.WeatherDetailsCard
import com.ritesh.weathersnap.ui.weather.components.WeatherEmptyCard

@Composable
fun WeatherScreen(
    onCreateReport: (Weather) -> Unit,
    onReportsClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val query by viewModel.inputText.collectAsStateWithLifecycle()
    val suggestions by viewModel.suggestions.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceDark)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderCard(
            title = "WeatherSnap",
            subtitle = "Live weather reports with camera evidence",
            trailing = {
                Button(
                    onClick = onReportsClick,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ForestGreen,
                        contentColor = MintSoft
                    )
                ) {
                    Text("Reports")
                }
            }
        )

        SearchCard(
            query = query,
            onQueryChange = viewModel::onQueryChange,
            onSearchClick = {}
        )

        AnimatedVisibility(visible = suggestions.isNotEmpty()) {
            SuggestionsCard(
                suggestions = suggestions,
                onPick = viewModel::onCitySelected
            )
        }

        AnimatedContent(
            targetState = state,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "weatherState"
        ) { current ->
            when (current) {
                WeatherUiState.Idle, WeatherUiState.Empty -> WeatherEmptyCard()
                WeatherUiState.Loading -> Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Lime)
                }
                is WeatherUiState.Success -> WeatherDetailsCard(
                    weather = current.weather,
                    onCreateReport = { onCreateReport(current.weather) }
                )
                is WeatherUiState.Error -> ErrorCard(current.message)
            }
        }
    }
}
