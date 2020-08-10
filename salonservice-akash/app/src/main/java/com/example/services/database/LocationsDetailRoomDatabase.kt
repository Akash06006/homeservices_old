package com.example.services.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = arrayOf(JobLocationsDetails::class, JobDetails::class),
    version = 1,
    exportSchema = false
)
public abstract class LocationsDetailRoomDatabase : RoomDatabase() {
    abstract fun jobLocationsDetailsDao() : JobLocationsDetailsDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE : LocationsDetailRoomDatabase? = null

        fun getDatabase(context : Context) : LocationsDetailRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocationsDetailRoomDatabase::class.java,
                    "location_database"
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}