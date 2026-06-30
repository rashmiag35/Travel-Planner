package com.example.travelplanner.presentation.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelplanner.data.local.TravelDatabase
import com.example.travelplanner.data.local.entity.Itinerary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardVM @Inject constructor(application: Application) : ViewModel() {
    private val itineraryDao = TravelDatabase.getDatabase(application).itineraryDao()

    val savedItineraries: StateFlow<List<Itinerary>> = itineraryDao.getAllItineraries()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
