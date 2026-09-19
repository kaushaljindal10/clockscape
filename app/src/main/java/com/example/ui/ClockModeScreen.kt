package com.example.ui

import android.app.Activity
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.background.AnimatedBackground
import com.example.background.BackgroundType
import com.example.clock.ClockDisplay
import com.example.clock.ClockStyleType
import com.example.clock.rememberCurrentTimeState
import com.example.data.ClockConfig
import kotlinx.coroutines.delay

@Composable
fun ClockModeScreen(
    config: ClockConfig,
    onExitClockMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val view = LocalView.current
    val activity = context as? Activity

    val currentBackground = BackgroundType.fromId(config.selectedBackgroundId)
    val currentClockStyle = ClockStyleType.fromId(config.selectedClockStyleId)
    val timeState = rememberCurrentTimeState(is24Hour = config.is24HourFormat)

    // Feature 2: Screen lock state
    var isLocked by remember { mutableStateOf(false) }

    // Feature 2.b: Opacity reduction after 5 seconds of inactivity
    var lastInteractionTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var isInactiveAfter5Sec by remember { mutableStateOf(false) }

    // Floating toast feedback (e.g. "Screen Locked", "Screen Unlocked", "Tap lock icon to exit")
    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    var showInitialExitHint by remember { mutableStateOf(true) }

    // Feature 1 & 2.c: Timer handling (e.g. 5m, 30m, 1h, 10h)
    val hasTimer = config.timerDurationMinutes > 0
    var secondsLeft by remember(config.timerDurationMinutes) {
        mutableIntStateOf(config.timerDurationMinutes * 60)
    }

    // Inactivity tracking (2.b): After 5 seconds of no interaction, reduce lock button opacity
    LaunchedEffect(lastInteractionTime) {
        isInactiveAfter5Sec = false
        delay(5000L)
        isInactiveAfter5Sec = true
    }

    // Lock button and overlay opacity animation
    val controlsAlpha by animateFloatAsState(
        targetValue = if (isInactiveAfter5Sec) 0.22f else 1.0f,
        animationSpec = tween(durationMillis = 600),
        label = "ControlsOpacity"
    )

    // Initial hint auto-hide after 3.5 seconds
    LaunchedEffect(Unit) {
        delay(3500L)
        showInitialExitHint = false
    }

    // Dismiss feedback message after 2.5 seconds
    LaunchedEffect(feedbackMessage) {
        if (feedbackMessage != null) {
            delay(2500L)
            feedbackMessage = null
        }
    }

    // Feature 1 & 2.c: Countdown timer loop
    LaunchedEffect(hasTimer, config.timerDurationMinutes) {
        if (hasTimer) {
            while (secondsLeft > 0) {
                delay(1000L)
                secondsLeft -= 1
            }
            // Timer expired!
            // 2.c: Automatically unlock screen
            isLocked = false
            lastInteractionTime = System.currentTimeMillis()
            feedbackMessage = "Timer Complete • Screen Unlocked"
        }
    }

    // Back button handling
    BackHandler {
        if (isLocked) {
            lastInteractionTime = System.currentTimeMillis()
            feedbackMessage = "Screen is locked • Tap the lock button to unlock"
        } else {
            onExitClockMode()
        }
    }

    // Immersive Full Screen and Keep Screen Awake
    DisposableEffect(Unit) {
        val window = activity?.window
        if (window != null) {
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            insetsController.hide(WindowInsetsCompat.Type.systemBars())

            if (config.keepScreenAwake) {
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }

        onDispose {
            val w = activity?.window
            if (w != null) {
                val insetsController = WindowCompat.getInsetsController(w, view)
                insetsController.show(WindowInsetsCompat.Type.systemBars())
                w.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }

    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    val orientation = configuration.orientation

    // Ensure system bars stay hidden on orientation rotation
    LaunchedEffect(orientation) {
        val window = activity?.window
        if (window != null) {
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            insetsController.hide(WindowInsetsCompat.Type.systemBars())
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("clock_mode_screen")
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                lastInteractionTime = System.currentTimeMillis()
                if (isLocked) {
                    // Feature 2: Screen is locked - keep showing clock, do not exit
                    feedbackMessage = "Screen is locked • Tap lock button to unlock"
                } else {
                    // Feature 2.a: In normal mode, tap anywhere exits
                    onExitClockMode()
                }
            }
    ) {
        // 1. Background Animation Layer
        AnimatedBackground(
            type = currentBackground,
            speed = config.animationSpeed,
            brightness = config.animationBrightness,
            customImagePath = config.customImagePath
        )

        // 2. Clock Display Layer (with Burn-In Protection Micro-Drift)
        ClockDisplay(
            style = currentClockStyle,
            timeState = timeState,
            config = config,
            enableBurnInDrift = true
        )

        // 3. Top-Right Corner Lock Button (Feature 2, 2.a, 2.b)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 28.dp, end = 24.dp)
                .graphicsLayer(alpha = controlsAlpha)
        ) {
            Surface(
                shape = CircleShape,
                color = if (isLocked) Color(0xD9DC2626) else Color(0x990F172A),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = if (isLocked) Color(0xFFFCA5A5) else Color(0x44FFFFFF)
                ),
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        lastInteractionTime = System.currentTimeMillis()
                        if (isLocked) {
                            // 2.a: Unlock screen and return to tap-anywhere-to-exit mode
                            isLocked = false
                            feedbackMessage = "Unlocked • Tap anywhere to exit"
                        } else {
                            // Lock screen to prevent accidental exit
                            isLocked = true
                            feedbackMessage = "Screen Locked • Clock will stay on"
                        }
                    }
                    .testTag("corner_lock_button")
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        imageVector = if (isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                        contentDescription = if (isLocked) "Unlock screen" else "Lock screen",
                        tint = if (isLocked) Color.White else Color(0xFFE2E8F0),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // 4. Timer Countdown Badge at Top-Start (Feature 1)
        if (hasTimer) {
            val hours = secondsLeft / 3600
            val minutes = (secondsLeft % 3600) / 60
            val secs = secondsLeft % 60
            val timeText = if (hours > 0) {
                String.format("%02d:%02d:%02d", hours, minutes, secs)
            } else {
                String.format("%02d:%02d", minutes, secs)
            }

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0x990F172A),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x33FFFFFF)),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 28.dp, start = 24.dp)
                    .graphicsLayer(alpha = controlsAlpha)
                    .testTag("clock_timer_indicator")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.HourglassTop,
                        contentDescription = "Timer",
                        tint = Color(0xFF60A5FA),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = timeText,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        // 5. User Feedback Message Toast
        AnimatedVisibility(
            visible = feedbackMessage != null,
            enter = fadeIn(tween(150)),
            exit = fadeOut(tween(250)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
        ) {
            feedbackMessage?.let { msg ->
                Surface(
                    color = Color(0xE60F172A),
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x44FFFFFF)),
                    shadowElevation = 8.dp,
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = msg,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    )
                }
            }
        }

        // 6. Initial "Tap anywhere to exit" instruction pill (first 3.5s)
        AnimatedVisibility(
            visible = showInitialExitHint && feedbackMessage == null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xB30F172A),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = if (isLocked) "Screen is locked" else "Tap anywhere to exit • Lock at top-right",
                    color = Color(0xFFE2E8F0),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}
