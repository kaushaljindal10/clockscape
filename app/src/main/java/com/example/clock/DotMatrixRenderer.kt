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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object DotMatrixPatterns {
    // 5x7 bitmap matrix: 7 lines of 5 bits
    val DIGITS: Map<Char, List<Int>> = mapOf(
        '0' to listOf(
            0b01110,
            0b10001,
            0b10011,
            0b10101,
            0b11001,
            0b10001,
            0b01110
        ),
        '1' to listOf(
            0b00100,
            0b01100,
            0b00100,
            0b00100,
            0b00100,
            0b00100,
            0b01110
        ),
        '2' to listOf(
            0b01110,
            0b10001,
            0b00001,
            0b00110,
            0b01000,
            0b10000,
            0b11111
        ),
        '3' to listOf(
            0b11110,
            0b00001,
            0b00001,
            0b01110,
            0b00001,
            0b00001,
            0b11110
        ),
        '4' to listOf(
            0b00010,
            0b00110,
            0b01010,
            0b10010,
            0b11111,
            0b00010,
            0b00010
        ),
        '5' to listOf(
            0b11111,
            0b10000,
            0b11110,
            0b00001,
            0b00001,
            0b10001,
            0b01110
        ),
        '6' to listOf(
            0b00110,
            0b01000,
            0b10000,
            0b11110,
            0b10001,
            0b10001,
            0b01110
        ),
        '7' to listOf(
            0b11111,
            0b00001,
            0b00010,
            0b00100,
            0b01000,
            0b01000,
            0b01000
        ),
        '8' to listOf(
            0b01110,
            0b10001,
            0b10001,
            0b01110,
            0b10001,
            0b10001,
            0b01110
        ),
        '9' to listOf(
            0b01110,
            0b10001,
            0b10001,
            0b01111,
            0b00001,
            0b00010,
            0b01100
        ),
        ':' to listOf(
            0b00000,
            0b00100,
            0b00000,
            0b00000,
            0b00100,
            0b00000,
            0b00000
        )
    )
}

@Composable
fun DotMatrixChar(
    char: Char,
    color: Color,
    width: Dp = 32.dp,
    height: Dp = 50.dp,
    cols: Int = 5,
    rows: Int = 7,
    modifier: Modifier = Modifier
) {
    val pattern = DotMatrixPatterns.DIGITS[char] ?: List(7) { 0 }
    val offColor = color.copy(alpha = 0.07f)

    Canvas(modifier = modifier.size(width, height)) {
        val dotRadius = (size.width / (cols * 2.3f)).coerceAtLeast(1.5f)
        val stepX = size.width / cols
        val stepY = size.height / rows

        for (r in 0 until rows) {
            val rowBits = pattern.getOrElse(r) { 0 }
            for (c in 0 until cols) {
                val isLit = ((rowBits shr (cols - 1 - c)) and 1) == 1
                val cx = (c + 0.5f) * stepX
                val cy = (r + 0.5f) * stepY

                drawCircle(
                    color = if (isLit) color else offColor,
                    radius = if (isLit) dotRadius else dotRadius * 0.75f,
                    center = Offset(cx, cy)
                )

                if (isLit) {
                    drawCircle(
                        color = color.copy(alpha = 0.35f),
                        radius = dotRadius * 1.6f,
                        center = Offset(cx, cy)
                    )
                }
            }
        }
    }
}

@Composable
fun DotMatrixClock(
    hours: String,
    minutes: String,
    seconds: String,
    showSeconds: Boolean,
    color: Color,
    scale: Float = 1.0f
) {
    val charW = (32 * scale).dp
    val charH = (52 * scale).dp
    val colonW = (16 * scale).dp
    val spacing = (4 * scale).dp

    Row(verticalAlignment = Alignment.CenterVertically) {
        DotMatrixChar(hours.getOrElse(0) { '0' }, color, charW, charH)
        Spacer(Modifier.width(spacing))
        DotMatrixChar(hours.getOrElse(1) { '0' }, color, charW, charH)
        Spacer(Modifier.width(spacing))

        DotMatrixChar(':', color, colonW, charH, cols = 3)
        Spacer(Modifier.width(spacing))

        DotMatrixChar(minutes.getOrElse(0) { '0' }, color, charW, charH)
        Spacer(Modifier.width(spacing))
        DotMatrixChar(minutes.getOrElse(1) { '0' }, color, charW, charH)

        if (showSeconds) {
            Spacer(Modifier.width(spacing))
            DotMatrixChar(':', color, colonW, charH, cols = 3)
            Spacer(Modifier.width(spacing))
            val secW = (22 * scale).dp
            val secH = (36 * scale).dp
            DotMatrixChar(seconds.getOrElse(0) { '0' }, color, secW, secH)
            Spacer(Modifier.width((2 * scale).dp))
            DotMatrixChar(seconds.getOrElse(1) { '0' }, color, secW, secH)
        }
    }
}
