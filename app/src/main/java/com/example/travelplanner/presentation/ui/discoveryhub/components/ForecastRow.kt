package com.example.travelplanner.presentation.ui.discoveryhub.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.travelplanner.domain.model.ForecastItem
import com.example.travelplanner.domain.model.Main
import kotlin.math.roundToInt

private fun forecastIcon(main: String?): ImageVector = when (main?.lowercase()) {
    "clear" -> Icons.Default.WbSunny
    "clouds" -> Icons.Default.WbCloudy
    "rain", "drizzle" -> Icons.Default.Grain
    "thunderstorm" -> Icons.Default.Thunderstorm
    "snow" -> Icons.Default.AcUnit
    "wind" -> Icons.Default.Air
    else -> Icons.Default.Cloud
}

@Composable
fun ForecastRow(forecastItems: List<ForecastItem>) {
    val dailyForecast = forecastItems.filterIndexed { index, _ -> index % 8 == 0 }.take(5)

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(dailyForecast) { item ->
            ForecastDayItem(item)
        }
    }
}

@Composable
fun ForecastDayItem(item: ForecastItem) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(72.dp)
                .padding(vertical = 12.dp, horizontal = 8.dp)
        ) {
            Text(
                text = item.dtTxt.substring(5, 10).replace("-", "/"),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Icon(
                imageVector = forecastIcon(item.weather.firstOrNull()?.main),
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${item.main.temp.roundToInt()}°",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Text(
                text = item.weather.firstOrNull()?.main ?: "—",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.65f)
            )
        }
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
