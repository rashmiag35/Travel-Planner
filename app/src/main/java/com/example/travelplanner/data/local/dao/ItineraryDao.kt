package com.example.travelplanner.data.local.dao

import androidx.room.*
import com.example.travelplanner.data.local.entity.Itinerary
import com.example.travelplanner.data.local.entity.SavedPlace
import kotlinx.coroutines.flow.Flow

@Dao
interface ItineraryDao {

    @Query("SELECT * FROM itineraries ORDER BY createdAt DESC")
    fun getAllItineraries(): Flow<List<Itinerary>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItinerary(itinerary: Itinerary): Long

    @Delete
    suspend fun deleteItinerary(itinerary: Itinerary)

    @Query("SELECT * FROM saved_places WHERE itineraryId = :itineraryId")
    fun getPlacesForItinerary(itineraryId: Long): Flow<List<SavedPlace>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedPlace(savedPlace: SavedPlace)

    @Delete
    suspend fun deleteSavedPlace(savedPlace: SavedPlace)
}
