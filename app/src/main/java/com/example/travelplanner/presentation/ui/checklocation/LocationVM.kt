package com.example.travelplanner.presentation.ui.checklocation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelplanner.data.network.RetrofitClient
import com.example.travelplanner.domain.model.WeatherResponse
import com.example.travelplanner.domain.repo.PermissionUiState
//import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//@HiltViewModel
class LocationVM: ViewModel() {
    private val _permissionState = MutableStateFlow<PermissionUiState>(PermissionUiState.Checking)
    val permissionState: StateFlow<PermissionUiState> = _permissionState

    private val _currentLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val currentLocation: StateFlow<Pair<Double, Double>?> = _currentLocation

    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
    val weatherData: StateFlow<WeatherResponse?> = _weatherData

    fun updatePermissionStatus(isGranted: Boolean, shouldShowRationale: Boolean) {
        _permissionState.value = when {
            isGranted -> PermissionUiState.Granted
            shouldShowRationale -> PermissionUiState.ShowRationale
            else -> PermissionUiState.DeniedPermanently
        }
    }

    fun updateLocation(latitude: Double, longitude: Double) {
        Log.d("TAG", "updateLocation: Latitude: $latitude, Longitude: $longitude")
        _currentLocation.value = Pair(latitude, longitude)
        fetchWeather(latitude, longitude)
    }

    private fun fetchWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.weatherApi.getWeather(lat, lon, "fdedde0353db046c465911df1d2390ce")
                _weatherData.value = response
                Log.d("TAG", "fetchWeather success: ${response.name}, Temp: ${response.main.temp}")
            } catch (e: Exception) {
                Log.e("TAG", "fetchWeather error", e)
            }
        }
    }
}
