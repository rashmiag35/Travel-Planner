package com.example.travelplanner.presentation.ui.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.travelplanner.data.local.entity.Itinerary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SavedTripsList(
    itineraries: List<Itinerary>,
    onItineraryClick: (String) -> Unit,
    onAddTripClick: (() -> Unit)? = null
) {
    val dateFormatter = SimpleDateFormat("MMM yyyy", Locale.getDefault())

    if (itineraries.isEmpty()) {
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
                if (onAddTripClick != null) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = onAddTripClick) {
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
}

@Preview(showBackground = true)
@Composable
fun SavedTripsListEmptyPreview() {
    MaterialTheme {
        SavedTripsList(itineraries = emptyList(), onItineraryClick = {}, onAddTripClick = {})
    }
}
