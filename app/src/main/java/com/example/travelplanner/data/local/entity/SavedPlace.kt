package com.example.travelplanner.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "saved_places",
    foreignKeys = [
        ForeignKey(
            entity = Itinerary::class,
            parentColumns = ["id"],
            childColumns = ["itineraryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["itineraryId"])]
)
data class SavedPlace(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val itineraryId: Long,
    val xid: String, // From OpenTripMap
    val name: String,
    val kinds: String? = null,
    val latitude: Double,
    val longitude: Double
)
