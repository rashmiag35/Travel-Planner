package com.example.travelplanner.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.travelplanner.data.local.dao.ItineraryDao
import com.example.travelplanner.data.local.entity.Itinerary
import com.example.travelplanner.data.local.entity.SavedPlace

@Database(entities = [Itinerary::class, SavedPlace::class], version = 1, exportSchema = false)
abstract class TravelDatabase : RoomDatabase() {

    abstract fun itineraryDao(): ItineraryDao

    companion object {
        @Volatile
        private var INSTANCE: TravelDatabase? = null

        fun getDatabase(context: Context): TravelDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TravelDatabase::class.java,
                    "travel_planner_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
