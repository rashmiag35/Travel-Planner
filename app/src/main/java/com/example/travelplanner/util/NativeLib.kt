package com.example.travelplanner.util

object NativeLib {
    init {
        System.loadLibrary("travelplanner")
    }

    external fun getMapsApiKey(): String
    external fun getWeatherApiKey(): String
    external fun getOpenTripMapApiKey(): String
}
