package com.example.travelplanner.presentation.ui.searchdestination

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.libraries.places.api.Places

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchDestinationScreen(
    onBackClick: () -> Unit,
    onPlaceSelected: (lat: Double, lon: Double, name: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchDestinationVM = viewModel()
) {
    val context = LocalContext.current
    val placesClient = remember {
        try {
            Places.createClient(context)
        } catch (e: Exception) {
            null
        }
    }
    var query by remember { mutableStateOf("") }
    val predictions by viewModel.predictions.collectAsStateWithLifecycle()
    val selectedPlace by viewModel.selectedPlace.collectAsStateWithLifecycle()

    LaunchedEffect(selectedPlace) {
        selectedPlace?.let { place ->
            val latLng = place.location
            if (latLng != null) {
                onPlaceSelected(latLng.latitude, latLng.longitude, place.displayName ?: "")
                viewModel.clearSelectedPlace()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Destination") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                    placesClient?.let { client ->
                        viewModel.getPredictions(it, client)
                    }
                },
                label = { Text("Enter city or place") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = {
                            query = ""
                            placesClient?.let { client ->
                                viewModel.getPredictions("", client)
                            }
                        }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(predictions) { prediction ->
                    ListItem(
                        headlineContent = { Text(prediction.getPrimaryText(null).toString()) },
                        supportingContent = { Text(prediction.getSecondaryText(null).toString()) },
                        modifier = Modifier.clickable {
                            placesClient?.let { client ->
                                viewModel.onPredictionSelected(prediction, client)
                            }
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchDestinationScreenPreview() {
    MaterialTheme {
        SearchDestinationScreen(onBackClick = {}, onPlaceSelected = { _, _, _ -> })
    }
}
