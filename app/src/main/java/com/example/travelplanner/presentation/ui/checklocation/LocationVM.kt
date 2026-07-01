package com.example.travelplanner.presentation.ui.checklocation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelplanner.data.network.RetrofitClient
import com.example.travelplanner.domain.model.WeatherResponse
import com.example.travelplanner.domain.repo.PermissionUiState
import com.example.travelplanner.util.NativeLib
import dagger.hilt.android.lifecycle.HiltViewModel
//import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationVM @Inject constructor() : ViewModel() {
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

    fun loadCurrentLocationWeather(context: Context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {

            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val providers = locationManager.getProviders(true)
            var bestLocation: android.location.Location? = null

            for (provider in providers) {
                val loc = locationManager.getLastKnownLocation(provider)
                print("Location: $loc")
                if (bestLocation == null || (loc != null && loc.accuracy < bestLocation.accuracy)) {
                    bestLocation = loc
                    print("Location: $bestLocation")
                }
            }

            bestLocation?.let {
                updateLocation(it.latitude, it.longitude)
            }
        } else
            print("Location permission not given")
    }

    fun updateLocation(latitude: Double, longitude: Double) {
        Log.d("TAG", "updateLocation: Latitude: $latitude, Longitude: $longitude")
        _currentLocation.value = Pair(latitude, longitude)
        fetchWeather(latitude, longitude)
    }

    private fun fetchWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.weatherApi.getWeather(lat, lon, NativeLib.getWeatherApiKey())
                _weatherData.value = response
                Log.d("TAG", "fetchWeather success: ${response.name}, Temp: ${response.main.temp}")
            } catch (e: Exception) {
                Log.e("TAG", "fetchWeather error", e)
            }
        }
    }
}
