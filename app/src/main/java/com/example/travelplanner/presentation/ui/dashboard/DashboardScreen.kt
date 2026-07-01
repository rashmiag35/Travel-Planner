package com.example.travelplanner.presentation.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.travelplanner.presentation.ui.checklocation.LocationVM
import com.example.travelplanner.presentation.ui.dashboard.components.SearchBarMock
import com.example.travelplanner.presentation.ui.dashboard.components.TripItemCard
import com.example.travelplanner.presentation.ui.dashboard.components.WeatherHeaderCard
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private fun greeting(): String {
    return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 5..11 -> "Good morning"
        in 12..17 -> "Good afternoon"
        else -> "Good evening"
    }
}

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
    val dateFormatter = SimpleDateFormat("MMM yyyy", Locale.getDefault())

    LaunchedEffect(Unit) {
        if (weatherData == null) {
            locationViewModel.loadCurrentLocationWeather(context)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // Greeting
        item {
            Column {
                Text(
                    text = greeting() + " ✈️",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
                Text(
                    text = "Travel Planner",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Weather card
        item {
            WeatherHeaderCard(weatherData)
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Search bar
        item {
            SearchBarMock(onClick = onSearchClick)
            Spacer(modifier = Modifier.height(28.dp))
        }

        // "My Trips" section header
        item {
            Text(
                text = "My Trips",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${savedItineraries.size} saved itinerar${if (savedItineraries.size == 1) "y" else "ies"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Empty state
        if (savedItineraries.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Luggage,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.25f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No trips planned yet",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Search for a destination and start\nbuilding your itinerary.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(onClick = onSearchClick) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(18.dp)
                                    .padding(end = 4.dp)
                            )
                            Text(text = "Plan a Trip")
                        }
                    }
                }
            }
        } else {
            // Trip cards — flat items at the same LazyColumn level
            items(savedItineraries) { trip ->
                TripItemCard(
                    title = trip.name,
                    date = dateFormatter.format(Date(trip.createdAt)),
                    onClick = { onItineraryClick(trip.name) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // Bottom padding
        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    MaterialTheme {
        DashboardScreen(onSearchClick = {}, onItineraryClick = {})
    }
}
