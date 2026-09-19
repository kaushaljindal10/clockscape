package com.example

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.AppScreen
import com.example.ui.BackgroundPickerScreen
import com.example.ui.ClockModeScreen
import com.example.ui.ClockPickerScreen
import com.example.ui.HomeScreen
import com.example.ui.MainViewModel
import com.example.ui.SettingsScreen
import com.example.ui.SplashScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val config by viewModel.config.collectAsState()

            LaunchedEffect(config.orientationMode) {
                try {
                    requestedOrientation = when (config.orientationMode) {
                        "LANDSCAPE" -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                        "PORTRAIT" -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        else -> ActivityInfo.SCREEN_ORIENTATION_FULL_USER
                    }
                } catch (_: Exception) {
                    // Handled gracefully if window manager restricts orientation changes
                }
            }

            MyApplicationTheme(darkTheme = config.isDarkMode) {
                AmbientClockApp(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun AmbientClockApp(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val config by viewModel.config.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()

    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "ScreenTransition",
        modifier = modifier
    ) { screen ->
        when (screen) {
            AppScreen.SPLASH -> {
                SplashScreen(
                    onSplashComplete = { viewModel.onSplashComplete() }
                )
            }

            AppScreen.HOME -> {
                HomeScreen(
                    config = config,
                    onStartClockMode = { viewModel.navigateTo(AppScreen.CLOCK_MODE) },
                    onChooseBackground = { viewModel.navigateTo(AppScreen.BACKGROUND_PICKER) },
                    onChooseClockStyle = { viewModel.navigateTo(AppScreen.CLOCK_PICKER) },
                    onOpenSettings = { viewModel.navigateTo(AppScreen.SETTINGS) },
                    onUpdateTimerDuration = { minutes -> viewModel.updateTimerDuration(minutes) }
                )
            }

            AppScreen.CLOCK_MODE -> {
                ClockModeScreen(
                    config = config,
                    onExitClockMode = { viewModel.navigateTo(AppScreen.HOME) }
                )
            }

            AppScreen.BACKGROUND_PICKER -> {
                BackgroundPickerScreen(
                    config = config,
                    onSelectBackground = { bg ->
                        viewModel.selectBackground(bg)
                    },
                    onUpdateSpeed = { speed ->
                        viewModel.updateAnimationSpeed(speed)
                    },
                    onBack = { viewModel.navigateTo(AppScreen.HOME) }
                )
            }

            AppScreen.CLOCK_PICKER -> {
                ClockPickerScreen(
                    config = config,
                    onSelectClockStyle = { style ->
                        viewModel.selectClockStyle(style)
                    },
                    onSelectColor = { name, hex ->
                        viewModel.updateColor(name, hex)
                    },
                    onUpdateSize = { size ->
                        viewModel.updateClockSize(size)
                    },
                    onUpdatePosition = { pos ->
                        viewModel.updateClockPosition(pos)
                    },
                    onUpdateOpacity = { opacity ->
                        viewModel.updateClockOpacity(opacity)
                    },
                    onToggleSeconds = { show ->
                        viewModel.toggleSeconds(show)
                    },
                    onToggleDate = { show ->
                        viewModel.toggleDate(show)
                    },
                    onToggleDay = { show ->
                        viewModel.toggleDay(show)
                    },
                    onToggle24Hour = { is24 ->
                        viewModel.toggle24Hour(is24)
                    },
                    onBack = { viewModel.navigateTo(AppScreen.HOME) }
                )
            }

            AppScreen.SETTINGS -> {
                SettingsScreen(
                    config = config,
                    onToggle24Hour = { viewModel.toggle24Hour(it) },
                    onToggleSeconds = { viewModel.toggleSeconds(it) },
                    onToggleDate = { viewModel.toggleDate(it) },
                    onToggleDay = { viewModel.toggleDay(it) },
                    onToggleKeepScreenAwake = { viewModel.toggleKeepScreenAwake(it) },
                    onToggleBurnInProtection = { viewModel.toggleBurnInProtection(it) },
                    onUpdateAnimationSpeed = { viewModel.updateAnimationSpeed(it) },
                    onUpdateAnimationBrightness = { viewModel.updateAnimationBrightness(it) },
                    onUpdateClockBrightness = { viewModel.updateClockBrightness(it) },
                    onToggleStartClockAutomatically = { viewModel.toggleStartClockAutomatically(it) },
                    onToggleDarkMode = { viewModel.toggleDarkMode(it) },
                    onUpdateOrientationMode = { viewModel.updateOrientationMode(it) },
                    onPreviewSplash = { viewModel.navigateTo(AppScreen.SPLASH) },
                    onResetAllSettings = { viewModel.resetAllSettings() },
                    onUpdateTimerDuration = { viewModel.updateTimerDuration(it) },
                    onBack = { viewModel.navigateTo(AppScreen.HOME) }
                )
            }
        }
    }
}
