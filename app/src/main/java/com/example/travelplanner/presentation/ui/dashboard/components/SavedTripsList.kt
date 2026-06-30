package com.example.travelplanner.presentation.ui.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.travelplanner.data.local.entity.Itinerary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SavedTripsList(
    itineraries: List<Itinerary>,
    onItineraryClick: (String) -> Unit
) {
    val dateFormatter = SimpleDateFormat("MMM yyyy", Locale.getDefault())

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(itineraries) { trip ->
            TripItemCard(
                title = trip.name,
                date = dateFormatter.format(Date(trip.createdAt)),
                onClick = { onItineraryClick(trip.name) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SavedTripsListPreview() {
    MaterialTheme {
        SavedTripsList(itineraries = emptyList(), onItineraryClick = {})
    }
}
