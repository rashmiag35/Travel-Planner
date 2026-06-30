package com.example.travelplanner.presentation.ui.discoveryhub.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.travelplanner.domain.model.ForecastItem
import com.example.travelplanner.domain.model.Main
import kotlin.math.roundToInt

@Composable
fun ForecastRow(forecastItems: List<ForecastItem>) {
    // Filter to get one item per day (e.g., at 12:00) or just take every 8th item (since data is 3-hourly)
    val dailyForecast = forecastItems.filterIndexed { index, _ -> index % 8 == 0 }.take(5)

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(dailyForecast) { item ->
            ForecastDayItem(item)
        }
    }
}

@Composable
fun ForecastDayItem(item: ForecastItem) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(70.dp)
    ) {
        Text(
            text = item.dtTxt.substring(5, 10), // Simplistic date MM-DD
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = "${item.main.temp.roundToInt()}°",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = item.weather.firstOrNull()?.main ?: "",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ForecastRowPreview() {
    val mockItems = (1..5).map { i ->
        ForecastItem(
            dt = 1625068800L + (i * 86400),
            main = Main(temp = 20.0 + i, feels_like = 20.0, temp_min = 18.0, temp_max = 25.0, pressure = 1013, humidity = 50),
            weather = listOf(),
            dtTxt = "2024-07-0$i 12:00:00"
        )
    }
    MaterialTheme {
        ForecastRow(forecastItems = mockItems)
    }
}
