package com.example.travelplanner.presentation.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelplanner.domain.model.*
import com.example.travelplanner.presentation.ui.checklocation.LocationVM
import kotlin.math.roundToInt

@Composable
fun CheckWeather(modifier: Modifier = Modifier, viewModel: LocationVM = viewModel()) {
    val context = LocalContext.current
    val weatherData by viewModel.weatherData.collectAsStateWithLifecycle()

    fun fetchCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val providers = locationManager.getProviders(true)
            var bestLocation: Location? = null
            for (provider in providers) {
                val loc = locationManager.getLastKnownLocation(provider)
                if (bestLocation == null || (loc != null && loc.accuracy < bestLocation.accuracy)) {
                    bestLocation = loc
                }
            }
            bestLocation?.let {
                viewModel.updateLocation(it.latitude, it.longitude)
            } ?: run {
                viewModel.updateLocation(0.0,0.0)
            }
        }
    }

    LaunchedEffect(Unit) {
        if (weatherData == null) {
            fetchCurrentLocation()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        weatherData?.let { weather ->
            WeatherDetails(weather)
        } ?: run {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun WeatherDetails(weather: WeatherResponse) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = weather.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 32.dp)
        )
        
        Text(
            text = weather.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "${(weather.main.temp - 273.15).roundToInt()}°",
            fontSize = 80.sp,
            fontWeight = FontWeight.Normal
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Max: ${(weather.main.temp_max - 273.15).roundToInt()}°",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Min: ${(weather.main.temp_min - 273.15).roundToInt()}°",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Weather Details",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                val details = listOfNotNull(
                    "Feels like" to "${(weather.main.feels_like - 273.15).roundToInt()}°C",
                    "Humidity" to "${weather.main.humidity}%",
                    "Pressure" to "${weather.main.pressure} hPa",
                    "Visibility" to "${weather.visibility / 1000} km",
                    "Wind Speed" to "${weather.wind.speed} m/s",
                    weather.rain?.let { "Rain (1h)" to "${it.oneH ?: 0.0} mm" }
                )

                details.forEachIndexed { index, detail ->
                    WeatherInfoRow(label = detail.first, value = detail.second)
                    if (index < details.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherPreview() {
    val mockWeather = WeatherResponse(
        coord = Coord(lon = -0.1257, lat = 51.5085),
        weather = listOf(
            WeatherInfo(
                id = 500,
                main = "Rain",
                description = "light rain",
                icon = "10d"
            )
        ),
        base = "stations",
        main = Main(
            temp = 291.41,
            feels_like = 291.09,
            temp_min = 290.4,
            temp_max = 292.04,
            pressure = 1023,
            humidity = 69,
            sea_level = 1023,
            grnd_level = 1019
        ),
        visibility = 10000,
        wind = Wind(speed = 0.45, deg = 287, gust = 1.34),
        rain = Rain(oneH = 0.28),
        clouds = Clouds(all = 100),
        dt = 1782805758,
        sys = Sys(
            type = 2,
            id = 2075535,
            country = "GB",
            sunrise = 1782791205,
            sunset = 1782850875
        ),
        timezone = 3600,
        id = 2643743,
        name = "London",
        cod = 200
    )
    MaterialTheme {
        WeatherDetails(weather = mockWeather)
    }
}
