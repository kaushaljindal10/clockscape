package com.example.clock

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. Large Minimal Digital
@Composable
fun LargeMinimalDigitalClock(
    timeState: TimeState,
    color: Color,
    showSeconds: Boolean,
    scale: Float = 1.0f
) {
    val mainFontSize = (86 * scale).sp
    val secFontSize = (32 * scale).sp

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "${timeState.hourString}:${timeState.minuteString}",
            color = color,
            fontSize = mainFontSize,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = (-1.5).sp
        )
        if (showSeconds) {
            Spacer(Modifier.width((10 * scale).dp))
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = timeState.secondString,
                    color = color.copy(alpha = 0.75f),
                    fontSize = secFontSize,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.SansSerif
                )
                if (!timeState.is24Hour) {
                    Text(
                        text = timeState.amPm,
                        color = color.copy(alpha = 0.5f),
                        fontSize = (16 * scale).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else if (!timeState.is24Hour) {
            Spacer(Modifier.width((8 * scale).dp))
            Text(
                text = timeState.amPm,
                color = color.copy(alpha = 0.6f),
                fontSize = (20 * scale).sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = (28 * scale).dp)
            )
        }
    }
}

// 2. Thin Elegant Digital
@Composable
fun ThinElegantDigitalClock(
    timeState: TimeState,
    color: Color,
    showSeconds: Boolean,
    scale: Float = 1.0f
) {
    val mainFontSize = (82 * scale).sp
    val secFontSize = (28 * scale).sp

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = timeState.hourString,
            color = color,
            fontSize = mainFontSize,
            fontWeight = FontWeight.ExtraLight,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = (4 * scale).sp
        )
        Text(
            text = ":",
            color = color.copy(alpha = 0.5f),
            fontSize = mainFontSize,
            fontWeight = FontWeight.Thin,
            modifier = Modifier.padding(horizontal = (6 * scale).dp)
        )
        Text(
            text = timeState.minuteString,
            color = color,
            fontSize = mainFontSize,
            fontWeight = FontWeight.ExtraLight,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = (4 * scale).sp
        )

        if (showSeconds) {
            Spacer(Modifier.width((10 * scale).dp))
            Text(
                text = timeState.secondString,
                color = color.copy(alpha = 0.5f),
                fontSize = secFontSize,
                fontWeight = FontWeight.Light,
                fontFamily = FontFamily.SansSerif
            )
        }
        if (!timeState.is24Hour) {
            Spacer(Modifier.width((8 * scale).dp))
            Text(
                text = timeState.amPm,
                color = color.copy(alpha = 0.45f),
                fontSize = (16 * scale).sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 2.sp
            )
        }
    }
}

// 3. Neon Digital
@Composable
fun NeonDigitalClock(
    timeState: TimeState,
    color: Color,
    showSeconds: Boolean,
    scale: Float = 1.0f
) {
    val mainFontSize = (82 * scale).sp
    val secFontSize = (30 * scale).sp

    val glowColor = color.copy(alpha = 0.95f)
    val outerGlow = color.copy(alpha = 0.4f)

    val neonStyle = TextStyle(
        fontSize = mainFontSize,
        fontWeight = FontWeight.ExtraBold,
        fontFamily = FontFamily.SansSerif,
        shadow = Shadow(
            color = outerGlow,
            offset = Offset(0f, 0f),
            blurRadius = 32f * scale
        )
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "${timeState.hourString}:${timeState.minuteString}",
            color = glowColor,
            style = neonStyle
        )
        if (showSeconds) {
            Spacer(Modifier.width((10 * scale).dp))
            Text(
                text = timeState.secondString,
                color = glowColor,
                fontSize = secFontSize,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    shadow = Shadow(
                        color = outerGlow,
                        offset = Offset(0f, 0f),
                        blurRadius = 20f * scale
                    )
                )
            )
        }
    }
}

// 10. Futuristic Digital (Cyberpunk HUD)
@Composable
fun FuturisticDigitalClock(
    timeState: TimeState,
    color: Color,
    showSeconds: Boolean,
    scale: Float = 1.0f
) {
    val mainFontSize = (72 * scale).sp
    val accentColor = color.copy(alpha = 0.85f)

    Box(
        modifier = Modifier
            .padding((12 * scale).dp)
            .border(
                width = 1.5.dp,
                color = color.copy(alpha = 0.25f),
                shape = CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp)
            )
            .background(
                color = Color(0x77070B14),
                shape = CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp)
            )
            .drawBehind {
                // Corner tech accents
                val len = 20f * scale
                val t = 3f
                // Top-left corner
                drawLine(color, Offset(0f, 0f), Offset(len, 0f), t, StrokeCap.Square)
                drawLine(color, Offset(0f, 0f), Offset(0f, len), t, StrokeCap.Square)
                // Bottom-right corner
                drawLine(color, Offset(size.width, size.height), Offset(size.width - len, size.height), t, StrokeCap.Square)
                drawLine(color, Offset(size.width, size.height), Offset(size.width, size.height - len), t, StrokeCap.Square)
            }
            .padding(horizontal = (24 * scale).dp, vertical = (16 * scale).dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // HUD Header Telemetry
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SYS.TIME // CHRONO",
                    color = color.copy(alpha = 0.45f),
                    fontSize = (10 * scale).sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                if (!timeState.is24Hour) {
                    Spacer(Modifier.width(16.dp))
                    Text(
                        text = "[${timeState.amPm}]",
                        color = accentColor,
                        fontSize = (11 * scale).sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height((4 * scale).dp))

            // Main Digits
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${timeState.hourString}:${timeState.minuteString}",
                    color = color,
                    fontSize = mainFontSize,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp
                )
                if (showSeconds) {
                    Spacer(Modifier.width((8 * scale).dp))
                    Text(
                        text = ":${timeState.secondString}",
                        color = accentColor,
                        fontSize = (32 * scale).sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            // HUD Footer Bar
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = (4 * scale).dp)
            ) {
                Box(
                    Modifier
                        .size((6 * scale).dp)
                        .background(color, RoundedCornerShape(2.dp))
                )
                Text(
                    text = "STATUS: ACTIVE // QUANTUM-LOCK",
                    color = color.copy(alpha = 0.35f),
                    fontSize = (9 * scale).sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
