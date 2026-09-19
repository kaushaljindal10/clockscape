package com.example.clock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ClockConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ClockDisplay(
    style: ClockStyleType,
    timeState: TimeState,
    config: ClockConfig,
    modifier: Modifier = Modifier,
    enableBurnInDrift: Boolean = true
) {
    var elapsedSeconds by remember { mutableFloatStateOf(0f) }

    // Very slow continuous micro-drift for burn-in protection
    LaunchedEffect(config.burnInProtection, enableBurnInDrift) {
        if (!config.burnInProtection || !enableBurnInDrift) {
            elapsedSeconds = 0f
            return@LaunchedEffect
        }
        while (isActive) {
            delay(1000L)
            elapsedSeconds += 1f
        }
    }

    val burnInOffsetX = if (config.burnInProtection && enableBurnInDrift) {
        (sin(elapsedSeconds * 0.005f) * 6f).dp
    } else {
        0.dp
    }
    val burnInOffsetY = if (config.burnInProtection && enableBurnInDrift) {
        (cos(elapsedSeconds * 0.007f) * 6f).dp
    } else {
        0.dp
    }

    val resolvedColor = config.getResolvedClockColor()
    val finalOpacity = (config.clockOpacity * config.clockBrightness).coerceIn(0.1f, 1.0f)
    val scale = config.clockSize.coerceIn(0.6f, 1.4f)

    val verticalAlignment = when (config.clockPosition) {
        "Top" -> Alignment.TopCenter
        "Bottom" -> Alignment.BottomCenter
        else -> Alignment.Center
    }

    val verticalPadding = when (config.clockPosition) {
        "Top" -> Modifier.padding(top = 48.dp)
        "Bottom" -> Modifier.padding(bottom = 64.dp)
        else -> Modifier
    }

    androidx.compose.foundation.layout.BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .then(verticalPadding)
            .offset(x = burnInOffsetX, y = burnInOffsetY)
            .alpha(finalOpacity),
        contentAlignment = verticalAlignment
    ) {
        val isLandscape = maxWidth > maxHeight
        // In landscape mode when height is constrained, slightly adapt scale so clock & date always fit
        val effectiveScale = if (isLandscape && maxHeight < 440.dp) {
            (scale * (maxHeight.value / 440f)).coerceIn(0.55f, 1.25f)
        } else {
            scale
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            // Clock Face
            when (style) {
                ClockStyleType.LARGE_MINIMAL_DIGITAL -> {
                    LargeMinimalDigitalClock(
                        timeState = timeState,
                        color = resolvedColor,
                        showSeconds = config.showSeconds,
                        scale = effectiveScale
                    )
                }
                ClockStyleType.THIN_ELEGANT_DIGITAL -> {
                    ThinElegantDigitalClock(
                        timeState = timeState,
                        color = resolvedColor,
                        showSeconds = config.showSeconds,
                        scale = effectiveScale
                    )
                }
                ClockStyleType.NEON_DIGITAL -> {
                    NeonDigitalClock(
                        timeState = timeState,
                        color = resolvedColor,
                        showSeconds = config.showSeconds,
                        scale = effectiveScale
                    )
                }
                ClockStyleType.RETRO_DIGITAL -> {
                    RetroSevenSegmentClock(
                        hours = timeState.hourString,
                        minutes = timeState.minuteString,
                        seconds = timeState.secondString,
                        showSeconds = config.showSeconds,
                        color = resolvedColor,
                        scale = effectiveScale
                    )
                }
                ClockStyleType.FLIP_CLOCK -> {
                    FlipClockDisplay(
                        hours = timeState.hourString,
                        minutes = timeState.minuteString,
                        seconds = timeState.secondString,
                        showSeconds = config.showSeconds,
                        color = resolvedColor,
                        scale = effectiveScale,
                        amPm = if (!config.is24HourFormat) timeState.amPm else ""
                    )
                }
                ClockStyleType.LARGE_ANALOG -> {
                    LargeAnalogClock(
                        timeState = timeState,
                        color = resolvedColor,
                        showSeconds = config.showSeconds,
                        size = (260 * effectiveScale).dp
                    )
                }
                ClockStyleType.MINIMAL_ANALOG -> {
                    MinimalAnalogClock(
                        timeState = timeState,
                        color = resolvedColor,
                        showSeconds = config.showSeconds,
                        size = (260 * effectiveScale).dp
                    )
                }
                ClockStyleType.ANALOG_DIGITAL -> {
                    AnalogDigitalHybridClock(
                        timeState = timeState,
                        color = resolvedColor,
                        showSeconds = config.showSeconds,
                        size = (260 * effectiveScale).dp
                    )
                }
                ClockStyleType.DOT_MATRIX -> {
                    DotMatrixClock(
                        hours = timeState.hourString,
                        minutes = timeState.minuteString,
                        seconds = timeState.secondString,
                        showSeconds = config.showSeconds,
                        color = resolvedColor,
                        scale = effectiveScale
                    )
                }
                ClockStyleType.FUTURISTIC_DIGITAL -> {
                    FuturisticDigitalClock(
                        timeState = timeState,
                        color = resolvedColor,
                        showSeconds = config.showSeconds,
                        scale = effectiveScale
                    )
                }
            }

            // Date & Day Display (below clock)
            if (config.showDay || config.showDate) {
                Spacer(Modifier.height((16 * effectiveScale).dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (config.showDay) {
                        Text(
                            text = timeState.dayOfWeek.uppercase(),
                            color = resolvedColor.copy(alpha = 0.8f),
                            fontSize = (14 * effectiveScale).sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                    if (config.showDay && config.showDate) {
                        Text(
                            text = "  •  ",
                            color = resolvedColor.copy(alpha = 0.4f),
                            fontSize = (14 * effectiveScale).sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    if (config.showDate) {
                        Text(
                            text = timeState.formattedDate,
                            color = resolvedColor.copy(alpha = 0.75f),
                            fontSize = (14 * effectiveScale).sp,
                            fontWeight = FontWeight.Normal,
                            letterSpacing = 1.sp,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
            }
        }
    }
}
