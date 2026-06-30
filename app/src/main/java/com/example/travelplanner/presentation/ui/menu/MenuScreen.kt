package com.example.travelplanner.presentation.ui.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.travelplanner.domain.model.CardItemData

@Composable
fun MenuScreen(modifier: Modifier = Modifier, onCardClick: (Int) -> Unit={}) {

    val cardsList = mutableListOf(
        CardItemData(1, "Search Destination", "Search your destination to travel"),
        CardItemData(2, "Explore Nearby Places", "Discover your nearby places."),
        CardItemData(3, "Check Local Weather", "Check the weather forecast for your location."),
        CardItemData(4, "Saved Itineraries", "View and manage your saved travel itineraries.")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Welcome to Travel Planner",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        cardsList.forEach { cardData ->
            ClickableCardItem(
                cardData = cardData,
                onCardClick = { clickedId ->
                    onCardClick(clickedId)
                }
            )
        }
    }
}

@Composable
fun ClickableCardItem(
    cardData: CardItemData,
    onCardClick: (Int) -> Unit
) {
    Card(
        onClick = { onCardClick(cardData.id) },
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = cardData.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = cardData.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    MenuScreen()
}