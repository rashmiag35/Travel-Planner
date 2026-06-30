package com.example.travelplanner.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.travelplanner.presentation.ui.checklocation.LocationPermissionScreen
import com.example.travelplanner.presentation.ui.menu.MenuScreen
import com.modivmedia.mydemoapp.presentation.ui.theme.TravelPlanner

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TravelPlanner {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "checkLocationPermission",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("checkLocationPermission") {
                            LocationPermissionScreen(
                                modifier = Modifier,
                                onPermissionGranted = {
                                    navController.navigate("menuScreen") {
                                        popUpTo("checkLocationPermission") {
                                            inclusive = true
                                        }
                                    }
                                })
                        }
                        composable("menuScreen") {
                            MenuScreen(
                                modifier = Modifier.fillMaxSize(),
                                onCardClick = { cardId ->
                                    when (cardId) {
                                        1 -> navController.navigate("searchDestination")
                                        2 -> navController.navigate("exploreNearbyPlaces")
                                        3 -> navController.navigate("CheckWeather")
                                        4 -> navController.navigate("SavedItinerary")
                                    }
                                }
                            )
                        }
                        composable("searchDestination") {

                        }
                        composable("exploreNearbyPlaces") {

                        }
                        composable("CheckWeather") {
                            CheckWeather(modifier = Modifier.fillMaxSize())
                        }
                        composable("SavedItinerary") {

                        }
                    }
                }
            }
        }
    }
}