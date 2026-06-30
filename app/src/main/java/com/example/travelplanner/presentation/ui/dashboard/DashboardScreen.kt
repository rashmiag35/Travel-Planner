package com.example.travelplanner.presentation.ui.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelplanner.presentation.ui.checklocation.LocationVM
import com.example.travelplanner.presentation.ui.dashboard.components.SavedTripsList
import com.example.travelplanner.presentation.ui.dashboard.components.SearchBarMock
import com.example.travelplanner.presentation.ui.dashboard.components.WeatherHeaderCard

@Composable
fun DashboardScreen(
    onSearchClick: () -> Unit,
    onItineraryClick: (String) -> Unit,
    locationViewModel: LocationVM = hiltViewModel(),
    dashboardViewModel: DashboardVM = hiltViewModel()
) {
    val context = LocalContext.current
    val weatherData by locationViewModel.weatherData.collectAsStateWithLifecycle()
    val savedItineraries by dashboardViewModel.savedItineraries.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (weatherData == null) {
            locationViewModel.loadCurrentLocationWeather(context)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Travel Planner",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp, top = 16.dp)
        )

        // 1. Header: Dynamic Weather Card
        WeatherHeaderCard(weatherData)

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Search Bar
        SearchBarMock(onClick = onSearchClick)

        Spacer(modifier = Modifier.height(32.dp))

        // 3. Body: My Saved Trips
        Text(
            text = "My Saved Trips",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        SavedTripsList(
            itineraries = savedItineraries,
            onItineraryClick = onItineraryClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    MaterialTheme {
        DashboardScreen(onSearchClick = {}, onItineraryClick = {})
    }
}
