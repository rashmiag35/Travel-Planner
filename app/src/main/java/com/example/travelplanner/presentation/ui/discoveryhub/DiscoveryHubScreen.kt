package com.example.travelplanner.presentation.ui.discoveryhub

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
                title = {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Forecast section
            if (forecastData != null) {
                item {
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        Text(
                            text = "5-Day Forecast",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        )
                        ForecastRow(forecastItems = forecastData!!.list)
                        Spacer(modifier = Modifier.height(20.dp))
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }

            // Things to do header
            item {
                Text(
                    text = "Things to Do",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }

            if (isLoading && nearbyPlaces.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(64.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            } else if (nearbyPlaces.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No nearby attractions found.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                    }
                }
            } else {
                items(nearbyPlaces) { place ->
                    val placeName = place.properties.name.ifEmpty { "Interesting Place" }
                    val isAlreadySaved = savedPlaceIds.contains(place.properties.xid)

                    AttractionItem(
                        place = place.copy(properties = place.properties.copy(name = placeName)),
                        isAdded = isAlreadySaved,
                        onAddClick = {
                            viewModel.savePlaceToItinerary(
                                name,
                                place.copy(properties = place.properties.copy(name = placeName))
                            )
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

@Preview(showBackground = true)
@Composable
fun DiscoveryHubScreenPreview() {
    MaterialTheme {
        DiscoveryHubScreen(lat = 48.8566, lon = 2.3522, name = "Paris")
    }
}
