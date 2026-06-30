package com.example.travelplanner.presentation.ui.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.travelplanner.domain.model.*
import kotlin.math.roundToInt

@Composable
fun WeatherHeaderCard(weather: WeatherResponse?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = weather?.name ?: "Detecting Location...",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                if (weather != null) {
                    Text(
                        text = "${weather.main.temp.roundToInt()}°C",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            if (weather != null) {
                Column(horizontalAlignment = Alignment.End) {
                    Icon(
                        imageVector = Icons.Default.Cloud,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = weather.weather.firstOrNull()?.main ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherHeaderCardPreview() {
    val mockWeather = WeatherResponse(
        coord = Coord(lon = -0.1257, lat = 51.5085),
        weather = listOf(WeatherInfo(id = 800, main = "Clear", description = "clear sky", icon = "01d")),
        base = "stations",
        main = Main(temp = 20.0, feels_like = 20.0, temp_min = 17.0, temp_max = 22.0, pressure = 1013, humidity = 50),
        visibility = 10000,
        wind = Wind(speed = 5.0, deg = 200),
        clouds = Clouds(all = 0),
        dt = 1625068800,
        sys = Sys(country = "GB", sunrise = 1625024400, sunset = 1625082000),
        timezone = 3600,
        id = 2643743,
        name = "London",
        cod = 200
    )
    MaterialTheme {
        WeatherHeaderCard(weather = mockWeather)
    }
}
