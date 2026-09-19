package com.example.background

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlinx.coroutines.isActive

@Composable
fun AnimatedBackground(
    type: BackgroundType,
    speed: Float = 1.0f,
    brightness: Float = 1.0f,
    customImagePath: String? = null,
    modifier: Modifier = Modifier,
    isPaused: Boolean = false
) {
    var timeSeconds by remember { mutableFloatStateOf(0f) }
    val particleState = remember(type) { BackgroundParticleState(100) }

    LaunchedEffect(isPaused, speed) {
        if (isPaused) return@LaunchedEffect
        var lastNanos = 0L
        while (isActive) {
            withFrameNanos { frameNanos ->
                if (lastNanos != 0L) {
                    val deltaSeconds = (frameNanos - lastNanos) / 1_000_000_000f
                    timeSeconds += deltaSeconds * speed
                }
                lastNanos = frameNanos
            }
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        renderBackground(
            type = type,
            scope = this,
            particleState = particleState,
            time = timeSeconds,
            brightness = brightness.coerceIn(0.1f, 1.0f)
        )
    }
}

private fun renderBackground(
    type: BackgroundType,
    scope: DrawScope,
    particleState: BackgroundParticleState,
    time: Float,
    brightness: Float
) {
    when (type) {
        BackgroundType.AURORA -> BackgroundRenderers.drawAurora(scope, time, brightness)
        BackgroundType.STARFIELD -> BackgroundRenderers.drawStarfield(scope, particleState, time, brightness)
        BackgroundType.FALLING_SNOW -> BackgroundRenderers.drawFallingSnow(scope, particleState, time, brightness)
        BackgroundType.RAIN -> BackgroundRenderers.drawRain(scope, particleState, time, brightness)
        BackgroundType.OCEAN_WAVES -> BackgroundRenderers.drawOceanWaves(scope, time, brightness)
        BackgroundType.FIRE_FLAMES -> BackgroundRenderers.drawFireFlames(scope, particleState, time, brightness)
        BackgroundType.FLOATING_PARTICLES -> BackgroundRenderers.drawFloatingParticles(scope, particleState, time, brightness)
        BackgroundType.COLOR_GRADIENT -> BackgroundRenderers.drawColorGradient(scope, time, brightness)
        BackgroundType.NEON_FLUID -> BackgroundRenderers.drawNeonFluid(scope, time, brightness)
        BackgroundType.GALAXY -> BackgroundRenderers.drawGalaxy(scope, particleState, time, brightness)
        BackgroundType.BUBBLES -> BackgroundRenderers.drawBubbles(scope, particleState, time, brightness)
        BackgroundType.DIGITAL_MATRIX -> BackgroundRenderers.drawDigitalMatrix(scope, particleState, time, brightness)
    }
}
