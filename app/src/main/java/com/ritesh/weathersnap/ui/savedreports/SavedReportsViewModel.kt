package com.ritesh.weathersnap.ui.savedreports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ritesh.weathersnap.data.repository.ReportRepository
import com.ritesh.weathersnap.domain.model.Report
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SavedReportsViewModel @Inject constructor(
    private val repository: ReportRepository
) : ViewModel() {

    val reports: StateFlow<List<Report>> = repository.observeSavedReports()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}
