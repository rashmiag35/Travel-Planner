package com.example.travelplanner.app

import android.app.Application
import com.example.travelplanner.util.NativeLib
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TravelPlannerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize the SDK with the New Places API enabled using key from NDK
        if (!Places.isInitialized()) {
            Places.initializeWithNewPlacesApiEnabled(applicationContext, NativeLib.getMapsApiKey())
        }
    }
}
