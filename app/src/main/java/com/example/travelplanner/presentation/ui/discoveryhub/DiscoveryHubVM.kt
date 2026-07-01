package com.example.travelplanner.presentation.ui.discoveryhub

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelplanner.data.local.TravelDatabase
import com.example.travelplanner.data.local.entity.Itinerary
import com.example.travelplanner.data.local.entity.SavedPlace
import com.example.travelplanner.data.network.RetrofitClient
import com.example.travelplanner.domain.model.ForecastResponse
import com.example.travelplanner.domain.model.opentripmap.Geometry
import com.example.travelplanner.domain.model.opentripmap.PlaceFeature
import com.example.travelplanner.domain.model.opentripmap.PlaceProperties
import com.example.travelplanner.util.NativeLib
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoveryHubVM @Inject constructor(application: Application) : ViewModel() {
    private val itineraryDao = TravelDatabase.getDatabase(application).itineraryDao()

    private val _forecastData = MutableStateFlow<ForecastResponse?>(null)
    val forecastData: StateFlow<ForecastResponse?> = _forecastData

    private val _nearbyPlaces = MutableStateFlow<List<PlaceFeature>>(emptyList())
    val nearbyPlaces: StateFlow<List<PlaceFeature>> = _nearbyPlaces

    private val _savedPlaceIds = MutableStateFlow<Set<String>>(emptySet())
    val savedPlaceIds: StateFlow<Set<String>> = _savedPlaceIds

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadCityData(lat: Double, lon: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Fetch Weather Forecast
                val forecast = RetrofitClient.weatherApi.getForecast(lat, lon, NativeLib.getWeatherApiKey())
                _forecastData.value = forecast

                // Fetch Nearby Places
                try {
                    val placesResponse = RetrofitClient.placesApi.getNearbyPlaces(
                        lat = lat,
                        lon = lon,
                        radius = 1000,
                        limit = 30,
                        apiKey = NativeLib.getOpenTripMapApiKey()
                    )
                    _nearbyPlaces.value = placesResponse.map { resp ->
                        PlaceFeature(
                            id = resp.xid,
                            properties = PlaceProperties(
                                xid = resp.xid,
                                name = resp.name,
                                dist = resp.dist,
                                rate = resp.rate,
                                kinds = resp.kinds
                            ),
                            geometry = Geometry(coordinates = listOf(resp.point.lon, resp.point.lat))
                        )
                    }
                } catch (e: Exception) {
                    print("API exception:: $e")
                    _nearbyPlaces.value = getMockPlaces()
                }

                // Initial load of saved places to sync icons
                refreshSavedPlaces()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun refreshSavedPlaces() {
        val allItineraries = itineraryDao.getAllItineraries().first()
        val allSavedPlaceIds = mutableSetOf<String>()
        allItineraries.forEach { itinerary ->
            itineraryDao.getPlacesForItinerary(itinerary.id).first().forEach { place ->
                allSavedPlaceIds.add(place.xid)
            }
        }
        _savedPlaceIds.value = allSavedPlaceIds
    }

    fun savePlaceToItinerary(cityName: String, place: PlaceFeature) {
        viewModelScope.launch {
            // 1. Find or Create Itinerary for this city
            val allItineraries = itineraryDao.getAllItineraries().first()
            var itinerary = allItineraries.find { it.name.contains(cityName, ignoreCase = true) }

            val itineraryId = itinerary?.id ?: itineraryDao.insertItinerary(Itinerary(name = "$cityName Trip"))

            // 2. Save the place
            itineraryDao.insertSavedPlace(
                SavedPlace(
                    itineraryId = itineraryId,
                    xid = place.properties.xid,
                    name = place.properties.name,
                    kinds = place.properties.kinds,
                    latitude = place.geometry.coordinates[1],
                    longitude = place.geometry.coordinates[0]
                )
            )

            // 3. Update local state for immediate UI feedback
            refreshSavedPlaces()
        }
    }

    private fun getMockPlaces(): List<PlaceFeature> {
        return listOf(
            PlaceFeature(
                id = "1",
                properties = PlaceProperties("1", "Historic Museum", 120.0, 5, "museums"),
                geometry = Geometry(listOf(0.0, 0.0))
            ),
            PlaceFeature(
                id = "2",
                properties = PlaceProperties("2", "Central Park", 450.0, 4, "parks"),
                geometry = Geometry(listOf(0.0, 0.0))
            ),
            PlaceFeature(
                id = "3",
                properties = PlaceProperties("3", "Old Town Square", 800.0, 5, "historic"),
                geometry = Geometry(listOf(0.0, 0.0))
            ),
            PlaceFeature(
                id = "4",
                properties = PlaceProperties("4", "Local Art Gallery", 1100.0, 3, "arts"),
                geometry = Geometry(listOf(0.0, 0.0))
            )
        )
    }
}
