package com.example.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ClockRepository(private val clockDao: ClockDao) {

    val config: Flow<ClockConfig> = clockDao.getConfig().map { storedConfig ->
        storedConfig ?: ClockConfig()
    }

    suspend fun updateConfig(newConfig: ClockConfig) {
        clockDao.saveConfig(newConfig.copy(id = 1))
    }

    fun saveConfigAsync(scope: CoroutineScope, newConfig: ClockConfig) {
        scope.launch(Dispatchers.IO) {
            clockDao.saveConfig(newConfig.copy(id = 1))
        }
    }
}
