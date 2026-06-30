package com.example.travelplanner.app

import android.app.Application
import com.google.android.libraries.places.api.Places

class TravelPlannerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize the SDK with the New Places API enabled
        if (!Places.isInitialized()) {
            Places.initializeWithNewPlacesApiEnabled(applicationContext, "AIzaSyApoh_GnGlKJy-bqgH0Cc-aNlaYxtGIKJk")
        }
    }
}
