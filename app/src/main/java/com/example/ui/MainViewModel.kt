package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.background.BackgroundType
import com.example.clock.ClockStyleType
import com.example.data.ClockConfig
import com.example.data.ClockDatabase
import com.example.data.ClockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    SPLASH,
    HOME,
    CLOCK_MODE,
    BACKGROUND_PICKER,
    CLOCK_PICKER,
    SETTINGS
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ClockRepository
    val config: StateFlow<ClockConfig>

    private val _currentScreen = MutableStateFlow(AppScreen.SPLASH)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    init {
        val database = ClockDatabase.getDatabase(application)
        repository = ClockRepository(database.clockDao())
        config = repository.config.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ClockConfig()
        )

        // Check if user has enabled "Start clock automatically"
        viewModelScope.launch {
            repository.config.collect { initialConfig ->
                if (initialConfig.startClockAutomatically && _currentScreen.value == AppScreen.HOME) {
                    _currentScreen.value = AppScreen.CLOCK_MODE
                }
            }
        }
    }

    fun onSplashComplete() {
        if (config.value.startClockAutomatically) {
            _currentScreen.value = AppScreen.CLOCK_MODE
        } else {
            _currentScreen.value = AppScreen.HOME
        }
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun selectBackground(background: BackgroundType) {
        val updated = config.value.copy(selectedBackgroundId = background.id)
        saveConfig(updated)
    }

    fun selectClockStyle(style: ClockStyleType) {
        val updated = config.value.copy(selectedClockStyleId = style.id)
        saveConfig(updated)
    }

    fun updateColor(name: String, hex: String) {
        val updated = config.value.copy(clockColorName = name, clockColorHex = hex)
        saveConfig(updated)
    }

    fun updateClockSize(size: Float) {
        val updated = config.value.copy(clockSize = size)
        saveConfig(updated)
    }

    fun updateClockPosition(position: String) {
        val updated = config.value.copy(clockPosition = position)
        saveConfig(updated)
    }

    fun updateClockOpacity(opacity: Float) {
        val updated = config.value.copy(clockOpacity = opacity)
        saveConfig(updated)
    }

    fun toggleSeconds(show: Boolean) {
        val updated = config.value.copy(showSeconds = show)
        saveConfig(updated)
    }

    fun toggleDate(show: Boolean) {
        val updated = config.value.copy(showDate = show)
        saveConfig(updated)
    }

    fun toggleDay(show: Boolean) {
        val updated = config.value.copy(showDay = show)
        saveConfig(updated)
    }

    fun toggle24Hour(is24: Boolean) {
        val updated = config.value.copy(is24HourFormat = is24)
        saveConfig(updated)
    }

    fun toggleKeepScreenAwake(keepAwake: Boolean) {
        val updated = config.value.copy(keepScreenAwake = keepAwake)
        saveConfig(updated)
    }

    fun toggleBurnInProtection(enabled: Boolean) {
        val updated = config.value.copy(burnInProtection = enabled)
        saveConfig(updated)
    }

    fun updateAnimationSpeed(speed: Float) {
        val updated = config.value.copy(animationSpeed = speed)
        saveConfig(updated)
    }

    fun updateAnimationBrightness(brightness: Float) {
        val updated = config.value.copy(animationBrightness = brightness)
        saveConfig(updated)
    }

    fun updateClockBrightness(brightness: Float) {
        val updated = config.value.copy(clockBrightness = brightness)
        saveConfig(updated)
    }

    fun toggleStartClockAutomatically(auto: Boolean) {
        val updated = config.value.copy(startClockAutomatically = auto)
        saveConfig(updated)
    }

    fun toggleDarkMode(isDark: Boolean) {
        val updated = config.value.copy(isDarkMode = isDark)
        saveConfig(updated)
    }

    fun updateOrientationMode(mode: String) {
        val updated = config.value.copy(orientationMode = mode)
        saveConfig(updated)
    }

    fun updateTimerDuration(minutes: Int) {
        val updated = config.value.copy(timerDurationMinutes = minutes.coerceAtLeast(0))
        saveConfig(updated)
    }

    fun resetAllSettings() {
        val default = ClockConfig()
        saveConfig(default)
    }

    private fun saveConfig(newConfig: ClockConfig) {
        viewModelScope.launch {
            repository.updateConfig(newConfig)
        }
    }
}
