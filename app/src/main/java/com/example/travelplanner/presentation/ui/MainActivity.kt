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
import com.example.travelplanner.presentation.ui.dashboard.DashboardScreen
import com.example.travelplanner.presentation.ui.discoveryhub.DiscoveryHubScreen
import com.example.travelplanner.presentation.ui.itinerarydetails.ItineraryDetailsScreen
import com.example.travelplanner.presentation.ui.searchdestination.SearchDestinationScreen
import com.modivmedia.mydemoapp.presentation.ui.theme.TravelPlanner
import androidx.navigation.NavType
import androidx.navigation.navArgument

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
                                    navController.navigate("dashboard") {
                                        popUpTo("checkLocationPermission") {
                                            inclusive = true
                                        }
                                    }
                                })
                        }
                        composable("dashboard") {
                            DashboardScreen(
                                onSearchClick = { navController.navigate("searchDestination") },
                                onItineraryClick = { tripName ->
                                    navController.navigate("itineraryDetails/$tripName")
                                }
                            )
                        }
                        composable(
                            route = "itineraryDetails/{tripName}",
                            arguments = listOf(
                                navArgument("tripName") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val tripName = backStackEntry.arguments?.getString("tripName") ?: ""
                            ItineraryDetailsScreen(
                                tripName = tripName,
                                onBackClick = { navController.popBackStack() },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        composable("searchDestination") {
                            SearchDestinationScreen(
                                onBackClick = { navController.popBackStack() },
                                onPlaceSelected = { lat, lon, name ->
                                    navController.navigate("discoveryHub/$lat/$lon/$name")
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        composable(
                            route = "discoveryHub/{lat}/{lon}/{name}",
                            arguments = listOf(
                                navArgument("lat") { type = NavType.StringType },
                                navArgument("lon") { type = NavType.StringType },
                                navArgument("name") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val lat = backStackEntry.arguments?.getString("lat")?.toDoubleOrNull() ?: 0.0
                            val lon = backStackEntry.arguments?.getString("lon")?.toDoubleOrNull() ?: 0.0
                            val name = backStackEntry.arguments?.getString("name") ?: ""
                            DiscoveryHubScreen(lat = lat, lon = lon, name = name, modifier = Modifier.fillMaxSize())
                        }
                    }
                }
            }
        }
    }
}