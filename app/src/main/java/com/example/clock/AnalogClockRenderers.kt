package com.example.clock

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LargeAnalogClock(
    timeState: TimeState,
    color: Color,
    showSeconds: Boolean,
    size: Dp = 270.dp,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val radius = this.size.minDimension / 2f
        val center = Offset(this.size.width / 2f, this.size.height / 2f)

        // Subtle outer bezel rim
        drawCircle(
            color = color.copy(alpha = 0.2f),
            radius = radius * 0.96f,
            center = center,
            style = Stroke(width = 3.5f)
        )
        drawCircle(
            color = color.copy(alpha = 0.05f),
            radius = radius * 0.92f,
            center = center
        )

        // 60 tick marks (hour ticks vs minute ticks)
        for (i in 0 until 60) {
            val angle = i * 6f * (PI / 180f).toFloat() - (PI / 2f).toFloat()
            val isHour = i % 5 == 0
            val innerR = if (isHour) radius * 0.78f else radius * 0.86f
            val outerR = radius * 0.90f
            val strokeW = if (isHour) 3.5f else 1.2f
            val tickAlpha = if (isHour) 0.95f else 0.4f

            val start = Offset(center.x + innerR * cos(angle), center.y + innerR * sin(angle))
            val end = Offset(center.x + outerR * cos(angle), center.y + outerR * sin(angle))

            drawLine(
                color = color.copy(alpha = tickAlpha),
                start = start,
                end = end,
                strokeWidth = strokeW,
                cap = StrokeCap.Round
            )
        }

        // Hour Hand
        drawHand(
            center = center,
            angleDeg = timeState.hourAngle,
            length = radius * 0.52f,
            width = 6.5f,
            color = color
        )

        // Minute Hand
        drawHand(
            center = center,
            angleDeg = timeState.minuteAngle,
            length = radius * 0.75f,
            width = 4.2f,
            color = color.copy(alpha = 0.95f)
        )

        // Second Hand
        if (showSeconds) {
            drawHand(
                center = center,
                angleDeg = timeState.secondAngle,
                length = radius * 0.84f,
                tailLength = radius * 0.18f,
                width = 2.0f,
                color = Color(0xFFEF4444) // Vibrant red second hand
            )
            // Second hand pivot ring
            drawCircle(
                color = Color(0xFFEF4444),
                radius = 4.5f,
                center = center
            )
        }

        // Center hub cap
        drawCircle(color = color, radius = 6f, center = center)
        drawCircle(color = Color(0xFF0F172A), radius = 2.5f, center = center)
    }
}

@Composable
fun MinimalAnalogClock(
    timeState: TimeState,
    color: Color,
    showSeconds: Boolean,
    size: Dp = 270.dp,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val radius = this.size.minDimension / 2f
        val center = Offset(this.size.width / 2f, this.size.height / 2f)

        // 12 sleek minimal hour indices
        for (i in 0 until 12) {
            val angle = i * 30f * (PI / 180f).toFloat() - (PI / 2f).toFloat()
            val isCardinal = i % 3 == 0
            val innerR = if (isCardinal) radius * 0.76f else radius * 0.84f
            val outerR = radius * 0.92f
            val strokeW = if (isCardinal) 3.5f else 1.8f
            val a = if (isCardinal) 0.9f else 0.45f

            val start = Offset(center.x + innerR * cos(angle), center.y + innerR * sin(angle))
            val end = Offset(center.x + outerR * cos(angle), center.y + outerR * sin(angle))

            drawLine(
                color = color.copy(alpha = a),
                start = start,
                end = end,
                strokeWidth = strokeW,
                cap = StrokeCap.Round
            )
        }

        // Hour Hand
        drawHand(
            center = center,
            angleDeg = timeState.hourAngle,
            length = radius * 0.50f,
            width = 5.0f,
            color = color
        )

        // Minute Hand
        drawHand(
            center = center,
            angleDeg = timeState.minuteAngle,
            length = radius * 0.74f,
            width = 3.0f,
            color = color.copy(alpha = 0.85f)
        )

        // Sweeping second hand
        if (showSeconds) {
            drawHand(
                center = center,
                angleDeg = timeState.secondAngle,
                length = radius * 0.82f,
                tailLength = radius * 0.15f,
                width = 1.6f,
                color = color.copy(alpha = 0.7f)
            )
        }

        // Center hub
        drawCircle(color = color, radius = 5f, center = center)
    }
}

@Composable
fun AnalogDigitalHybridClock(
    timeState: TimeState,
    color: Color,
    showSeconds: Boolean,
    size: Dp = 270.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        MinimalAnalogClock(
            timeState = timeState,
            color = color,
            showSeconds = showSeconds,
            size = size
        )

        // Digital inset badge placed at 6 o'clock position
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = (size.value * 0.18f).dp)
                .background(
                    color = Color(0x990A0E1A),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            val secText = if (showSeconds) ":${timeState.secondString}" else ""
            val amPmText = if (!timeState.is24Hour) " ${timeState.amPm}" else ""
            Text(
                text = "${timeState.hourString}:${timeState.minuteString}$secText$amPmText",
                color = color,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

private fun DrawScope.drawHand(
    center: Offset,
    angleDeg: Float,
    length: Float,
    width: Float,
    color: Color,
    tailLength: Float = 0f
) {
    val rad = angleDeg * (PI / 180f).toFloat() - (PI / 2f).toFloat()
    val tip = Offset(center.x + length * cos(rad), center.y + length * sin(rad))
    val tail = if (tailLength > 0f) {
        Offset(center.x - tailLength * cos(rad), center.y - tailLength * sin(rad))
    } else {
        center
    }

    drawLine(
        color = color,
        start = tail,
        end = tip,
        strokeWidth = width,
        cap = StrokeCap.Round
    )
}
