package com.example.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

/**
 * Aesthetic entrance splash screen for Clockscape.
 * Features an atmospheric celestial radial backdrop, an animated precision chrono-dial,
 * luminescent typography reveal, and a subtle glowing progress tracker.
 */
@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Animatable entrance values
    val emblemScale = remember { Animatable(0.4f) }
    val emblemAlpha = remember { Animatable(0f) }
    val titleAlpha = remember { Animatable(0f) }
    val titleOffsetY = remember { Animatable(24f) }
    val subtitleAlpha = remember { Animatable(0f) }
    val bottomProgressAlpha = remember { Animatable(0f) }

    // Continuous ambient animations
    val infiniteTransition = rememberInfiniteTransition(label = "SplashInfinite")
    val sweepAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "SweepRotation"
    )
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "CorePulse"
    )
    val ambientGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GlowPulse"
    )

    // Staggered orchestration sequence
    LaunchedEffect(Unit) {
        launch {
            emblemAlpha.animateTo(1f, animationSpec = tween(700, easing = FastOutSlowInEasing))
        }
        launch {
            emblemScale.animateTo(1f, animationSpec = spring(dampingRatio = 0.68f, stiffness = 200f))
        }

        delay(350)
        launch {
            titleAlpha.animateTo(1f, animationSpec = tween(650, easing = FastOutSlowInEasing))
        }
        launch {
            titleOffsetY.animateTo(0f, animationSpec = spring(dampingRatio = 0.75f, stiffness = 250f))
        }

        delay(300)
        launch {
            subtitleAlpha.animateTo(1f, animationSpec = tween(500, easing = FastOutSlowInEasing))
        }
        launch {
            bottomProgressAlpha.animateTo(1f, animationSpec = tween(500))
        }

        // Auto transition after entrance has played gracefully
        delay(1800)
        onSplashComplete()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F172A),
                        Color(0xFF070B14),
                        Color(0xFF020408)
                    )
                )
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                // Instant skip on tap
                onSplashComplete()
            }
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        // Ambient soft atmospheric glow in background
        Box(
            modifier = Modifier
                .size(340.dp)
                .scale(pulseScale)
                .alpha(ambientGlowAlpha)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF0284C7).copy(alpha = 0.35f),
                            Color(0xFF38BDF8).copy(alpha = 0.10f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Center Content Column
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 28.dp)
        ) {
            // Chrono-Dial Logo Centerpiece
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .scale(emblemScale.value * pulseScale)
                    .alpha(emblemAlpha.value),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val radius = size.minDimension / 2f
                    val center = Offset(size.width / 2f, size.height / 2f)

                    // Outer delicate ring
                    drawCircle(
                        color = Color(0xFF1E293B),
                        radius = radius * 0.94f,
                        style = Stroke(width = 1.5.dp.toPx())
                    )

                    // Inner dashed / tick orbit ring
                    drawCircle(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                Color(0xFF38BDF8),
                                Color(0xFF6366F1),
                                Color(0xFF0284C7),
                                Color(0xFF38BDF8)
                            )
                        ),
                        radius = radius * 0.82f,
                        style = Stroke(width = 2.dp.toPx())
                    )

                    // 12 Chrono Ticks
                    for (i in 0 until 12) {
                        val angleRad = Math.toRadians((i * 30).toDouble())
                        val isCardinal = i % 3 == 0
                        val innerR = if (isCardinal) radius * 0.66f else radius * 0.72f
                        val outerR = radius * 0.80f

                        val startX = center.x + (innerR * cos(angleRad)).toFloat()
                        val startY = center.y + (innerR * sin(angleRad)).toFloat()
                        val endX = center.x + (outerR * cos(angleRad)).toFloat()
                        val endY = center.y + (outerR * sin(angleRad)).toFloat()

                        drawLine(
                            color = if (isCardinal) Color(0xFF67E8F9) else Color(0xFF64748B),
                            start = Offset(startX, startY),
                            end = Offset(endX, endY),
                            strokeWidth = if (isCardinal) 2.5.dp.toPx() else 1.5.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }

                    // Rotating Sweeping Meridian Hand
                    val sweepRad = Math.toRadians(sweepAngle.toDouble())
                    val handLength = radius * 0.76f
                    val handEndX = center.x + (handLength * cos(sweepRad)).toFloat()
                    val handEndY = center.y + (handLength * sin(sweepRad)).toFloat()

                    // Glow line for sweep hand
                    drawLine(
                        brush = Brush.linearGradient(
                            colors = listOf(Color.Transparent, Color(0xFF38BDF8), Color(0xFFE0F2FE)),
                            start = center,
                            end = Offset(handEndX, handEndY)
                        ),
                        start = center,
                        end = Offset(handEndX, handEndY),
                        strokeWidth = 2.5.dp.toPx(),
                        cap = StrokeCap.Round
                    )

                    // Luminous hand tip
                    drawCircle(
                        color = Color(0xFFE0F2FE),
                        radius = 3.5.dp.toPx(),
                        center = Offset(handEndX, handEndY)
                    )

                    // Center luminous pivot
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFF38BDF8), Color(0xFF0369A1))
                        ),
                        radius = radius * 0.16f
                    )
                    drawCircle(
                        color = Color.White,
                        radius = radius * 0.06f
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            // Brand Wordmark
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alpha(titleAlpha.value)
            ) {
                Text(
                    text = "CLOCKSCAPE",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 6.sp,
                    fontFamily = FontFamily.SansSerif
                )

                Spacer(Modifier.height(10.dp))

                // Elegant divider with glowing node
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.width(180.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color.Transparent, Color(0xFF38BDF8))
                                )
                            )
                    )
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .background(Color(0xFF38BDF8), CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF38BDF8), Color.Transparent)
                                )
                            )
                    )
                }

                Spacer(Modifier.height(12.dp))

                // Subtitle
                Text(
                    text = "ATMOSPHERIC TIMEPIECE & SCREENSAVER",
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp,
                    modifier = Modifier.alpha(subtitleAlpha.value)
                )
            }
        }

        // Bottom Subtle Readiness Indicator
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 28.dp)
                .alpha(bottomProgressAlpha.value)
        ) {
            // Elegant pulsing dot wave
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val dotScale1 = 0.8f + (pulseScale - 0.96f) * 4f
                val dotScale2 = 1.2f - (pulseScale - 0.96f) * 4f
                val dotScale3 = 0.9f + (pulseScale - 0.96f) * 3f

                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .scale(dotScale1)
                        .background(Color(0xFF38BDF8), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .scale(dotScale2)
                        .background(Color(0xFF60A5FA), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .scale(dotScale3)
                        .background(Color(0xFF818CF8), CircleShape)
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Tap to skip",
                color = Color(0xFF64748B),
                fontSize = 11.sp,
                letterSpacing = 0.8.sp
            )
        }
    }
}
