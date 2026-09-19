package com.example.clock

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Renders a classic 7-segment digit with active/inactive segments
 */
@Composable
fun SevenSegmentDigit(
    char: Char,
    color: Color,
    width: Dp = 32.dp,
    height: Dp = 60.dp,
    modifier: Modifier = Modifier
) {
    val segments = when (char) {
        '0' -> booleanArrayOf(true, true, true, false, true, true, true)
        '1' -> booleanArrayOf(false, false, true, false, false, true, false)
        '2' -> booleanArrayOf(true, false, true, true, true, false, true)
        '3' -> booleanArrayOf(true, false, true, true, false, true, true)
        '4' -> booleanArrayOf(false, true, true, true, false, true, false)
        '5' -> booleanArrayOf(true, true, false, true, false, true, true)
        '6' -> booleanArrayOf(true, true, false, true, true, true, true)
        '7' -> booleanArrayOf(true, false, true, false, false, true, false)
        '8' -> booleanArrayOf(true, true, true, true, true, true, true)
        '9' -> booleanArrayOf(true, true, true, true, false, true, true)
        else -> booleanArrayOf(false, false, false, false, false, false, false)
    }

    val inactiveColor = color.copy(alpha = 0.08f)

    Canvas(modifier = modifier.size(width, height)) {
        val w = size.width
        val h = size.height
        val t = (w * 0.16f).coerceAtLeast(3f) // thickness
        val gap = t * 0.25f
        val halfH = h / 2f

        // Segment A (top horizontal)
        drawRect(
            color = if (segments[0]) color else inactiveColor,
            topLeft = Offset(t + gap, 0f),
            size = Size(w - 2 * (t + gap), t)
        )
        // Segment B (top right vertical)
        drawRect(
            color = if (segments[1]) color else inactiveColor,
            topLeft = Offset(0f, t + gap),
            size = Size(t, halfH - t - 1.5f * gap)
        )
        // Segment C (top left vertical)
        drawRect(
            color = if (segments[2]) color else inactiveColor,
            topLeft = Offset(w - t, t + gap),
            size = Size(t, halfH - t - 1.5f * gap)
        )
        // Segment D (middle horizontal)
        drawRect(
            color = if (segments[3]) color else inactiveColor,
            topLeft = Offset(t + gap, halfH - t / 2f),
            size = Size(w - 2 * (t + gap), t)
        )
        // Segment E (bottom left vertical)
        drawRect(
            color = if (segments[4]) color else inactiveColor,
            topLeft = Offset(0f, halfH + t / 2f + gap),
            size = Size(t, halfH - t - 1.5f * gap)
        )
        // Segment F (bottom right vertical)
        drawRect(
            color = if (segments[5]) color else inactiveColor,
            topLeft = Offset(w - t, halfH + t / 2f + gap),
            size = Size(t, halfH - t - 1.5f * gap)
        )
        // Segment G (bottom horizontal)
        drawRect(
            color = if (segments[6]) color else inactiveColor,
            topLeft = Offset(t + gap, h - t),
            size = Size(w - 2 * (t + gap), t)
        )
    }
}

@Composable
fun SevenSegmentColon(
    color: Color,
    width: Dp = 14.dp,
    height: Dp = 60.dp,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(width, height)) {
        val r = (size.width * 0.35f).coerceIn(2.5f, 6f)
        val cx = size.width / 2f
        drawCircle(color = color, radius = r, center = Offset(cx, size.height * 0.35f))
        drawCircle(color = color, radius = r, center = Offset(cx, size.height * 0.65f))
    }
}

@Composable
fun RetroSevenSegmentClock(
    hours: String,
    minutes: String,
    seconds: String,
    showSeconds: Boolean,
    color: Color,
    scale: Float = 1.0f
) {
    val digitW = (36 * scale).dp
    val digitH = (68 * scale).dp
    val colonW = (16 * scale).dp
    val spacing = (4 * scale).dp

    Row(verticalAlignment = Alignment.CenterVertically) {
        SevenSegmentDigit(hours.getOrElse(0) { '0' }, color, digitW, digitH)
        Spacer(Modifier.width(spacing))
        SevenSegmentDigit(hours.getOrElse(1) { '0' }, color, digitW, digitH)
        Spacer(Modifier.width(spacing))

        SevenSegmentColon(color, colonW, digitH)
        Spacer(Modifier.width(spacing))

        SevenSegmentDigit(minutes.getOrElse(0) { '0' }, color, digitW, digitH)
        Spacer(Modifier.width(spacing))
        SevenSegmentDigit(minutes.getOrElse(1) { '0' }, color, digitW, digitH)

        if (showSeconds) {
            Spacer(Modifier.width(spacing))
            SevenSegmentColon(color, colonW, digitH)
            Spacer(Modifier.width(spacing))
            val secW = (24 * scale).dp
            val secH = (46 * scale).dp
            SevenSegmentDigit(seconds.getOrElse(0) { '0' }, color, secW, secH)
            Spacer(Modifier.width((2 * scale).dp))
            SevenSegmentDigit(seconds.getOrElse(1) { '0' }, color, secW, secH)
        }
    }
}
