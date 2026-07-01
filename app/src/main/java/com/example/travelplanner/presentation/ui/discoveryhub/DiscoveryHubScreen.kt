package com.example.travelplanner.presentation.ui.discoveryhub

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelplanner.presentation.ui.discoveryhub.components.AttractionItem
import com.example.travelplanner.presentation.ui.discoveryhub.components.ForecastRow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoveryHubScreen(
    lat: Double,
    lon: Double,
    name: String,
    modifier: Modifier = Modifier,
    viewModel: DiscoveryHubVM = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val forecastData by viewModel.forecastData.collectAsStateWithLifecycle()
    val nearbyPlaces by viewModel.nearbyPlaces.collectAsStateWithLifecycle()
    val savedPlaceIds by viewModel.savedPlaceIds.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(lat, lon) {
        viewModel.loadCityData(lat, lon)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            forecastData?.let {
                Text(
                    text = "5-Day Forecast",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                ForecastRow(forecastItems = it.list)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Things to Do",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (isLoading && nearbyPlaces.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                if (nearbyPlaces.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No nearby attractions found.")
                    }
                } else {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(nearbyPlaces) { place ->
                            val placeName = place.properties.name.ifEmpty { "Interesting Place" }
                            val isAlreadySaved = savedPlaceIds.contains(place.properties.xid)

                            AttractionItem(
                                place = place.copy(properties = place.properties.copy(name = placeName)),
                                isAdded = isAlreadySaved,
                                onAddClick = {
                                    viewModel.savePlaceToItinerary(name, place.copy(properties = place.properties.copy(name = placeName)))
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Added $placeName to your trip!")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiscoveryHubScreenPreview() {
    MaterialTheme {
        DiscoveryHubScreen(lat = 48.8566, lon = 2.3522, name = "Paris")
    }
}
