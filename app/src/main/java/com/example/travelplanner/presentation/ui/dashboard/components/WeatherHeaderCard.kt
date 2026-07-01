package com.example.travelplanner.presentation.ui.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Water
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.example.travelplanner.domain.model.*
import kotlin.math.roundToInt

private fun weatherIcon(main: String?): ImageVector = when (main?.lowercase()) {
    "clear" -> Icons.Default.WbSunny
    "clouds" -> Icons.Default.WbCloudy
    "rain", "drizzle" -> Icons.Default.Grain
    "thunderstorm" -> Icons.Default.Thunderstorm
    "wind" -> Icons.Default.Air
    else -> Icons.Default.Cloud
}

@Composable
fun WeatherHeaderCard(weather: WeatherResponse?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        if (weather == null) {
            // Skeleton / loading state
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Detecting your location…",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        } else {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left: location + temperature
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = weather.name,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${weather.main.temp.roundToInt()}°C",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Feels like ${weather.main.feels_like.roundToInt()}°C",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }

                    // Right: weather icon + condition
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = weatherIcon(weather.weather.firstOrNull()?.main),
                            contentDescription = null,
                            modifier = Modifier.size(52.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = weather.weather.firstOrNull()?.main ?: "",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.15f)
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Bottom stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherStatItem(
                        icon = Icons.Default.Water,
                        label = "Humidity",
                        value = "${weather.main.humidity}%"
                    )
                    WeatherStatItem(
                        icon = Icons.Default.Air,
                        label = "Wind",
                        value = "${weather.wind.speed.roundToInt()} m/s"
                    )
                    WeatherStatItem(
                        icon = Icons.Default.WbSunny,
                        label = "High / Low",
                        value = "${weather.main.temp_max.roundToInt()}° / ${weather.main.temp_min.roundToInt()}°"
                    )
                }
            }
        }
    }
}

@Composable
private fun WeatherStatItem(icon: ImageVector, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherHeaderCardPreview() {
    val mockWeather = WeatherResponse(
        coord = Coord(lon = -0.1257, lat = 51.5085),
        weather = listOf(WeatherInfo(id = 800, main = "Clear", description = "clear sky", icon = "01d")),
        base = "stations",
        main = Main(temp = 20.0, feels_like = 18.5, temp_min = 17.0, temp_max = 22.0, pressure = 1013, humidity = 62),
        visibility = 10000,
        wind = Wind(speed = 4.2, deg = 200),
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
