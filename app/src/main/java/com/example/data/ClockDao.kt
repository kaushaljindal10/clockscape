package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ClockDao {
    @Query("SELECT * FROM clock_config WHERE id = 1 LIMIT 1")
    fun getConfig(): Flow<ClockConfig?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveConfig(config: ClockConfig)
}
