package com.example.travelplanner.presentation.ui.itinerarydetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelplanner.data.local.TravelDatabase
import com.example.travelplanner.data.local.entity.Itinerary
import com.example.travelplanner.data.local.entity.SavedPlace
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ItineraryDetailsVM(application: Application) : AndroidViewModel(application) {
    private val itineraryDao = TravelDatabase.getDatabase(application).itineraryDao()

    private val _places = MutableStateFlow<List<SavedPlace>>(emptyList())
    val places: StateFlow<List<SavedPlace>> = _places

    fun loadItineraryDetails(tripName: String) {
        viewModelScope.launch {
            val allItineraries = itineraryDao.getAllItineraries().first()
            val itinerary = allItineraries.find { it.name == tripName }
            
            itinerary?.let {
                itineraryDao.getPlacesForItinerary(it.id).collect {
                    _places.value = it
                }
            }
        }
    }
}
