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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Individual split-flap digit card.
 * Each digit gets its own dedicated card to guarantee zero overlapping of digits.
 */
@Composable
fun FlipCard(
    text: String,
    textColor: Color,
    width: Dp = 44.dp,
    height: Dp = 72.dp,
    fontSize: Float = 48f,
    modifier: Modifier = Modifier
) {
    val cornerRadius = (6 * (width.value / 44f)).coerceAtLeast(3f).dp

    Box(
        modifier = modifier
            .size(width, height)
            .shadow(4.dp, RoundedCornerShape(cornerRadius))
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF262C38),
                        Color(0xFF1B202A),
                        Color(0xFF13171F),
                        Color(0xFF0D1016)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = Color(0x33FFFFFF),
                shape = RoundedCornerShape(cornerRadius)
            )
            .drawBehind {
                val midY = size.height / 2f

                // Top flap subtle gradient highlight
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.08f),
                            Color.Transparent
                        ),
                        startY = 0f,
                        endY = midY
                    ),
                    size = Size(size.width, midY)
                )

                // Bottom flap subtle drop shadow right below the flap seam
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.45f),
                            Color.Transparent
                        ),
                        startY = midY,
                        endY = midY + (size.height * 0.25f)
                    ),
                    topLeft = Offset(0f, midY),
                    size = Size(size.width, size.height * 0.25f)
                )

                // Horizontal split seam
                drawLine(
                    color = Color(0xFF07090C),
                    start = Offset(0f, midY),
                    end = Offset(size.width, midY),
                    strokeWidth = 2.5f
                )
                drawLine(
                    color = Color(0x22FFFFFF),
                    start = Offset(0f, midY + 1.2f),
                    end = Offset(size.width, midY + 1.2f),
                    strokeWidth = 1f
                )

                // Left & right mechanical hinge notches
                val notchRadius = (width.toPx() * 0.06f).coerceIn(2.5f, 4.5f)
                drawCircle(
                    color = Color(0xFF090C10),
                    radius = notchRadius,
                    center = Offset(0f, midY)
                )
                drawCircle(
                    color = Color(0xFF090C10),
                    radius = notchRadius,
                    center = Offset(size.width, midY)
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            maxLines = 1,
            softWrap = false
        )
    }
}

/**
 * Split-Flap Flip Clock Display.
 * Renders individual cards for each digit (hours, minutes, seconds)
 * ensuring crisp legibility and completely preventing digits from overlapping.
 */
@Composable
fun FlipClockDisplay(
    hours: String,
    minutes: String,
    seconds: String,
    showSeconds: Boolean,
    color: Color,
    scale: Float = 1.0f,
    amPm: String = ""
) {
    val hDigit1 = hours.getOrElse(0) { '0' }.toString()
    val hDigit2 = hours.getOrElse(1) { '0' }.toString()

    val mDigit1 = minutes.getOrElse(0) { '0' }.toString()
    val mDigit2 = minutes.getOrElse(1) { '0' }.toString()

    val sDigit1 = seconds.getOrElse(0) { '0' }.toString()
    val sDigit2 = seconds.getOrElse(1) { '0' }.toString()

    val cardW = (42 * scale).dp
    val cardH = (68 * scale).dp
    val fontSz = 44f * scale

    val secCardW = (30 * scale).dp
    val secCardH = (50 * scale).dp
    val secFontSz = 30f * scale

    val digitSpacing = (4 * scale).dp
    val sectionSpacing = (10 * scale).dp

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Hours Group (Digit 1 + Digit 2)
        Row(verticalAlignment = Alignment.CenterVertically) {
            FlipCard(
                text = hDigit1,
                textColor = color,
                width = cardW,
                height = cardH,
                fontSize = fontSz
            )
            Spacer(Modifier.width(digitSpacing))
            FlipCard(
                text = hDigit2,
                textColor = color,
                width = cardW,
                height = cardH,
                fontSize = fontSz
            )
        }

        Spacer(Modifier.width(sectionSpacing))

        // Colon separator dots between hours and minutes
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 2.dp)
        ) {
            Box(
                Modifier
                    .size((6 * scale).dp)
                    .clip(RoundedCornerShape(50))
                    .background(color.copy(alpha = 0.9f))
            )
            Spacer(Modifier.height((12 * scale).dp))
            Box(
                Modifier
                    .size((6 * scale).dp)
                    .clip(RoundedCornerShape(50))
                    .background(color.copy(alpha = 0.9f))
            )
        }

        Spacer(Modifier.width(sectionSpacing))

        // Minutes Group (Digit 1 + Digit 2)
        Row(verticalAlignment = Alignment.CenterVertically) {
            FlipCard(
                text = mDigit1,
                textColor = color,
                width = cardW,
                height = cardH,
                fontSize = fontSz
            )
            Spacer(Modifier.width(digitSpacing))
            FlipCard(
                text = mDigit2,
                textColor = color,
                width = cardW,
                height = cardH,
                fontSize = fontSz
            )
        }

        // Optional Seconds Group
        if (showSeconds) {
            Spacer(Modifier.width(sectionSpacing))

            // Colon separator dots before seconds
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 1.dp)
            ) {
                Box(
                    Modifier
                        .size((4.5f * scale).dp)
                        .clip(RoundedCornerShape(50))
                        .background(color.copy(alpha = 0.75f))
                )
                Spacer(Modifier.height((9 * scale).dp))
                Box(
                    Modifier
                        .size((4.5f * scale).dp)
                        .clip(RoundedCornerShape(50))
                        .background(color.copy(alpha = 0.75f))
                )
            }

            Spacer(Modifier.width(sectionSpacing))

            Row(verticalAlignment = Alignment.CenterVertically) {
                FlipCard(
                    text = sDigit1,
                    textColor = color.copy(alpha = 0.92f),
                    width = secCardW,
                    height = secCardH,
                    fontSize = secFontSz
                )
                Spacer(Modifier.width((3 * scale).dp))
                FlipCard(
                    text = sDigit2,
                    textColor = color.copy(alpha = 0.92f),
                    width = secCardW,
                    height = secCardH,
                    fontSize = secFontSz
                )
            }
        }

        // Optional AM/PM Indicator
        if (amPm.isNotEmpty()) {
            Spacer(Modifier.width((8 * scale).dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF161B22))
                    .border(0.8.dp, Color(0x33FFFFFF), RoundedCornerShape(4.dp))
                    .padding(horizontal = (6 * scale).dp, vertical = (4 * scale).dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = amPm,
                    color = color.copy(alpha = 0.85f),
                    fontSize = (11 * scale).sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}
