package com.example.travelplanner.data.network

import com.example.travelplanner.domain.model.opentripmap.PlaceDetails
import com.example.travelplanner.domain.model.opentripmap.PlaceResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenTripMapApiService {

    @GET("places/radius")
    suspend fun getNearbyPlaces(
        @Query("radius") radius: Int = 1000,
        @Query("lon") lon: Double,
        @Query("lat") lat: Double,
        @Query("apikey") apiKey: String,
        @Query("kinds") kinds: String = "interesting_places",
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 30
    ): List<PlaceResponse>

    @GET("places/xid/{xid}")
    suspend fun getPlaceDetails(
        @Path("xid") xid: String,
        @Query("apikey") apiKey: String
    ): PlaceDetails
}
