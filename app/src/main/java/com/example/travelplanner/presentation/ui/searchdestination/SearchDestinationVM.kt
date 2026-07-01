package com.example.travelplanner.presentation.ui.searchdestination

import androidx.lifecycle.ViewModel
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearchDestinationVM @Inject constructor() : ViewModel() {
    private val _predictions = MutableStateFlow<List<AutocompletePrediction>>(emptyList())
    val predictions: StateFlow<List<AutocompletePrediction>> = _predictions

    private val _selectedPlace = MutableStateFlow<Place?>(null)
    val selectedPlace: StateFlow<Place?> = _selectedPlace

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getPredictions(query: String, placesClient: PlacesClient) {
        if (query.isEmpty()) {
            _predictions.value = emptyList()
            return
        }

        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                _predictions.value = response.autocompletePredictions
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }

    fun onPredictionSelected(prediction: AutocompletePrediction, placesClient: PlacesClient) {
        _isLoading.value = true
        val placeFields = listOf(Place.Field.ID, Place.Field.DISPLAY_NAME, Place.Field.LOCATION)
        val request = FetchPlaceRequest.newInstance(prediction.placeId, placeFields)

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                _selectedPlace.value = response.place
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                _isLoading.value = false
            }
    }

    fun clearSelectedPlace() {
        _isLoading.value = false
        _selectedPlace.value = null
        _predictions.value = emptyList()
    }
}
