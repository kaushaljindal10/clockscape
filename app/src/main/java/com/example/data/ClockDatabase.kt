package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ClockConfig::class], version = 7, exportSchema = false)
abstract class ClockDatabase : RoomDatabase() {
    abstract fun clockDao(): ClockDao

    companion object {
        @Volatile
        private var INSTANCE: ClockDatabase? = null

        fun getDatabase(context: Context): ClockDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClockDatabase::class.java,
                    "ambient_clock_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
