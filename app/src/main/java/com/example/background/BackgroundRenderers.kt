package com.example.background

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * State holder for pre-allocated background particles to ensure zero-allocation per frame
 * and ultra-low CPU/battery usage.
 */
class BackgroundParticleState(val count: Int = 100) {
    val x = FloatArray(count)
    val y = FloatArray(count)
    val z = FloatArray(count)
    val speed = FloatArray(count)
    val size = FloatArray(count)
    val alpha = FloatArray(count)
    val seed = FloatArray(count)

    init {
        val rand = Random(42)
        for (i in 0 until count) {
            x[i] = rand.nextFloat()
            y[i] = rand.nextFloat()
            z[i] = 0.1f + rand.nextFloat() * 0.9f
            speed[i] = 0.5f + rand.nextFloat() * 1.5f
            size[i] = 2f + rand.nextFloat() * 6f
            alpha[i] = 0.3f + rand.nextFloat() * 0.7f
            seed[i] = rand.nextFloat() * 100f
        }
    }
}

object BackgroundRenderers {

    // 1. AURORA
    fun drawAurora(scope: DrawScope, time: Float, brightness: Float) {
        val w = scope.size.width
        val h = scope.size.height
        // Deep nocturnal background
        scope.drawRect(
            brush = Brush.verticalGradient(
                listOf(Color(0xFF030712), Color(0xFF0A1128), Color(0xFF061A24))
            ),
            size = scope.size
        )

        val auroraColors = listOf(
            Color(0xFF10B981).copy(alpha = 0.28f * brightness),
            Color(0xFF06B6D4).copy(alpha = 0.22f * brightness),
            Color(0xFF8B5CF6).copy(alpha = 0.20f * brightness),
            Color(0xFF3B82F6).copy(alpha = 0.18f * brightness)
        )

        val segments = 24
        val dx = w / segments
        auroraColors.forEachIndexed { index, color ->
            val path = Path()
            val phase = time * (0.8f + index * 0.25f) + index * 1.8f
            val baseHeight = h * (0.22f + index * 0.15f)

            path.moveTo(0f, h)
            path.lineTo(0f, baseHeight)

            for (i in 0..segments) {
                val px = i * dx
                val wave1 = sin(px * 0.004f + phase) * (h * 0.08f)
                val wave2 = cos(px * 0.008f - phase * 0.6f) * (h * 0.05f)
                val wave3 = sin((px + index * 80f) * 0.002f + phase * 1.2f) * (h * 0.04f)
                val py = baseHeight + wave1 + wave2 + wave3
                path.lineTo(px, py)
            }
            path.lineTo(w, h)
            path.close()

            scope.drawPath(
                path = path,
                brush = Brush.verticalGradient(
                    colors = listOf(color, Color.Transparent),
                    startY = baseHeight - h * 0.1f,
                    endY = baseHeight + h * 0.35f
                )
            )
        }
    }

    // 2. STARFIELD
    fun drawStarfield(
        scope: DrawScope,
        particles: BackgroundParticleState,
        time: Float,
        brightness: Float
    ) {
        val w = scope.size.width
        val h = scope.size.height
        val cx = w / 2f
        val cy = h / 2f

        scope.drawRect(Color(0xFF05070F), size = scope.size)

        val starCount = particles.count.coerceAtMost(90)
        for (i in 0 until starCount) {
            val speedFactor = 0.08f * particles.speed[i]
            var cz = (particles.z[i] - (time * speedFactor) % 1.0f)
            if (cz <= 0.02f) cz += 1.0f

            val px = cx + ((particles.x[i] - 0.5f) * w * 1.8f) / cz
            val py = cy + ((particles.y[i] - 0.5f) * h * 1.8f) / cz

            if (px in 0f..w && py in 0f..h) {
                val starSize = (1f / cz).coerceIn(1.2f, 4.5f)
                val twinkle = (0.5f + 0.5f * sin(time * 3f + particles.seed[i])).coerceIn(0.2f, 1f)
                val a = (particles.alpha[i] * twinkle * brightness * (1f - cz * 0.5f)).coerceIn(0.1f, 1f)
                val starColor = if (i % 5 == 0) Color(0xFF93C5FD) else Color(0xFFFFFFFF)
                scope.drawCircle(
                    color = starColor.copy(alpha = a),
                    radius = starSize,
                    center = Offset(px, py)
                )
            }
        }
    }

    // 3. FALLING SNOW
    fun drawFallingSnow(
        scope: DrawScope,
        particles: BackgroundParticleState,
        time: Float,
        brightness: Float
    ) {
        val w = scope.size.width
        val h = scope.size.height

        scope.drawRect(
            brush = Brush.verticalGradient(
                listOf(Color(0xFF0A0E17), Color(0xFF111827), Color(0xFF1E293B))
            ),
            size = scope.size
        )

        val count = particles.count.coerceAtMost(75)
        for (i in 0 until count) {
            val fallProgress = (particles.y[i] + time * 0.08f * particles.speed[i]) % 1.0f
            val py = fallProgress * h
            val drift = sin(time * 1.2f + particles.seed[i]) * 24f
            val px = (particles.x[i] * w + drift + w) % w

            val radius = particles.size[i] * 0.65f
            val a = (particles.alpha[i] * brightness).coerceIn(0.15f, 0.9f)

            scope.drawCircle(
                color = Color.White.copy(alpha = a),
                radius = radius,
                center = Offset(px, py)
            )
        }
    }

    // 4. RAIN
    fun drawRain(
        scope: DrawScope,
        particles: BackgroundParticleState,
        time: Float,
        brightness: Float
    ) {
        val w = scope.size.width
        val h = scope.size.height

        scope.drawRect(Color(0xFF070B12), size = scope.size)

        val count = particles.count.coerceAtMost(70)
        val streakLength = 32f
        val slant = 6f

        for (i in 0 until count) {
            val fall = (particles.y[i] + time * 0.65f * particles.speed[i]) % 1.0f
            val py = fall * (h + streakLength)
            val px = (particles.x[i] * w + fall * slant * 15f) % w

            val a = (particles.alpha[i] * 0.6f * brightness).coerceIn(0.1f, 0.75f)
            scope.drawLine(
                color = Color(0xFF7DD3FC).copy(alpha = a),
                start = Offset(px, py - streakLength),
                end = Offset(px + slant, py),
                strokeWidth = 1.4f
            )

            // Ripple splash near bottom
            if (py >= h - 40f && i % 3 == 0) {
                val splashProgress = ((py - (h - 40f)) / 40f).coerceIn(0f, 1f)
                val splashRadius = splashProgress * 14f
                val splashAlpha = ((1f - splashProgress) * 0.4f * brightness).coerceIn(0f, 1f)
                scope.drawOval(
                    color = Color(0xFFBAE6FD).copy(alpha = splashAlpha),
                    topLeft = Offset(px - splashRadius, h - 8f - splashRadius * 0.3f),
                    size = Size(splashRadius * 2f, splashRadius * 0.6f),
                    style = Stroke(width = 1f)
                )
            }
        }
    }

    // 5. OCEAN WAVES
    fun drawOceanWaves(scope: DrawScope, time: Float, brightness: Float) {
        val w = scope.size.width
        val h = scope.size.height

        // Deep ocean background
        scope.drawRect(
            brush = Brush.verticalGradient(
                listOf(Color(0xFF030A18), Color(0xFF071B33), Color(0xFF0B2D4D))
            ),
            size = scope.size
        )

        val waveLayers = listOf(
            Triple(0.55f, Color(0xFF0369A1), 0.7f),
            Triple(0.68f, Color(0xFF0284C7), 1.0f),
            Triple(0.80f, Color(0xFF0EA5E9), 1.3f),
            Triple(0.90f, Color(0xFF38BDF8), 1.7f)
        )

        val segments = 24
        val dx = w / segments
        waveLayers.forEachIndexed { idx, (yRatio, baseColor, speedMult) ->
            val path = Path()
            val baseY = h * yRatio
            val phase = time * speedMult + idx * 2.2f

            path.moveTo(0f, h)
            path.lineTo(0f, baseY)

            for (i in 0..segments) {
                val px = i * dx
                val wave1 = sin(px * 0.006f + phase) * (h * 0.035f)
                val wave2 = cos(px * 0.012f - phase * 0.8f) * (h * 0.02f)
                path.lineTo(px, baseY + wave1 + wave2)
            }
            path.lineTo(w, h)
            path.close()

            val alpha = (0.35f + idx * 0.15f) * brightness
            scope.drawPath(
                path = path,
                brush = Brush.verticalGradient(
                    colors = listOf(baseColor.copy(alpha = alpha), baseColor.copy(alpha = alpha * 0.3f)),
                    startY = baseY - h * 0.05f,
                    endY = h
                )
            )
        }
    }

    // 6. FIRE & FLAMES
    fun drawFireFlames(
        scope: DrawScope,
        particles: BackgroundParticleState,
        time: Float,
        brightness: Float
    ) {
        val w = scope.size.width
        val h = scope.size.height

        scope.drawRect(Color(0xFF0A0302), size = scope.size)

        // Bottom hearth glow
        scope.drawOval(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFDC2626).copy(alpha = 0.45f * brightness),
                    Color(0xFFEA580C).copy(alpha = 0.25f * brightness),
                    Color.Transparent
                ),
                center = Offset(w / 2f, h),
                radius = w * 0.7f
            ),
            topLeft = Offset(-w * 0.2f, h - h * 0.35f),
            size = Size(w * 1.4f, h * 0.7f)
        )

        val count = particles.count.coerceAtMost(80)
        for (i in 0 until count) {
            val rise = (particles.y[i] + time * 0.22f * particles.speed[i]) % 1.0f
            val py = h - (rise * (h * 0.85f))
            val sway = sin(time * 2.5f + particles.seed[i]) * (w * 0.06f)
            val px = (particles.x[i] * w * 0.8f + w * 0.1f + sway)

            val life = 1f - rise
            val radius = particles.size[i] * (0.8f + life * 0.8f)
            val color = when {
                life > 0.7f -> Color(0xFFFEF08A) // bright yellow
                life > 0.4f -> Color(0xFFF97316) // orange
                else -> Color(0xFFEF4444) // deep red ember
            }

            val a = (life * particles.alpha[i] * brightness).coerceIn(0f, 1f)
            scope.drawCircle(
                color = color.copy(alpha = a),
                radius = radius,
                center = Offset(px, py)
            )
        }
    }

    // 7. FLOATING PARTICLES (Bokeh)
    fun drawFloatingParticles(
        scope: DrawScope,
        particles: BackgroundParticleState,
        time: Float,
        brightness: Float
    ) {
        val w = scope.size.width
        val h = scope.size.height

        scope.drawRect(
            brush = Brush.verticalGradient(
                listOf(Color(0xFF0F0B1A), Color(0xFF1A122E), Color(0xFF090611))
            ),
            size = scope.size
        )

        val count = particles.count.coerceAtMost(45)
        for (i in 0 until count) {
            val floatY = (particles.y[i] - time * 0.035f * particles.speed[i] + 100f) % 1.0f
            val py = floatY * h
            val floatX = (particles.x[i] + sin(time * 0.8f + particles.seed[i]) * 0.08f + 100f) % 1.0f
            val px = floatX * w

            val pulse = (0.7f + 0.3f * sin(time * 1.5f + particles.seed[i])).coerceIn(0.3f, 1.2f)
            val radius = particles.size[i] * 3.5f * pulse
            val color = when (i % 4) {
                0 -> Color(0xFFA855F7)
                1 -> Color(0xFFEC4899)
                2 -> Color(0xFF6366F1)
                else -> Color(0xFF38BDF8)
            }
            val a = (particles.alpha[i] * 0.35f * brightness * pulse).coerceIn(0.05f, 0.6f)

            // Outer soft glow
            scope.drawCircle(
                color = color.copy(alpha = a * 0.4f),
                radius = radius * 1.8f,
                center = Offset(px, py)
            )
            // Core
            scope.drawCircle(
                color = color.copy(alpha = a),
                radius = radius,
                center = Offset(px, py)
            )
        }
    }

    // 8. COLOR GRADIENT (Morphing Liquid Gradient)
    fun drawColorGradient(scope: DrawScope, time: Float, brightness: Float) {
        val w = scope.size.width
        val h = scope.size.height

        val x1 = (0.5f + 0.4f * sin(time * 0.4f)) * w
        val y1 = (0.5f + 0.4f * cos(time * 0.5f)) * h

        val x2 = (0.5f + 0.4f * cos(time * 0.35f + 1.5f)) * w
        val y2 = (0.5f + 0.4f * sin(time * 0.45f + 2.0f)) * h

        scope.drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF4338CA).copy(alpha = 0.75f * brightness),
                    Color(0xFF0F172A)
                ),
                center = Offset(x1, y1),
                radius = w * 0.85f
            ),
            size = scope.size
        )

        scope.drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFBE185D).copy(alpha = 0.55f * brightness),
                    Color.Transparent
                ),
                center = Offset(x2, y2),
                radius = w * 0.75f
            ),
            size = scope.size
        )

        scope.drawRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF064E3B).copy(alpha = 0.35f * brightness),
                    Color(0xFF1E1B4B).copy(alpha = 0.45f * brightness),
                    Color(0xFF000000).copy(alpha = 0.7f)
                ),
                start = Offset(0f, 0f),
                end = Offset(w, h)
            ),
            size = scope.size
        )
    }

    // 9. NEON FLUID (Swirling Plasma Currents)
    fun drawNeonFluid(scope: DrawScope, time: Float, brightness: Float) {
        val w = scope.size.width
        val h = scope.size.height
        val cx = w / 2f
        val cy = h / 2f

        scope.drawRect(Color(0xFF05050A), size = scope.size)

        val loops = 5
        for (k in 0 until loops) {
            val path = Path()
            val phase = time * (0.6f + k * 0.15f) + k * 1.3f
            val baseRadius = (w * 0.22f) + k * (w * 0.08f)
            val steps = 60

            for (i in 0..steps) {
                val theta = (i / steps.toFloat()) * (2f * PI.toFloat())
                val rOffset = sin(theta * 3f + phase) * (w * 0.07f) +
                        cos(theta * 2f - phase * 0.8f) * (w * 0.04f)
                val r = baseRadius + rOffset
                val px = cx + r * cos(theta)
                val py = cy + r * sin(theta) * 1.2f

                if (i == 0) path.moveTo(px, py) else path.lineTo(px, py)
            }
            path.close()

            val color = if (k % 2 == 0) Color(0xFF06B6D4) else Color(0xFFEC4899)
            val alpha = (0.28f + k * 0.08f) * brightness
            scope.drawPath(
                path = path,
                color = color.copy(alpha = alpha * 0.5f),
                style = Stroke(width = 8f)
            )
            scope.drawPath(
                path = path,
                color = Color.White.copy(alpha = alpha),
                style = Stroke(width = 2.5f)
            )
        }
    }

    // 10. GALAXY (Spiral Arms)
    fun drawGalaxy(
        scope: DrawScope,
        particles: BackgroundParticleState,
        time: Float,
        brightness: Float
    ) {
        val w = scope.size.width
        val h = scope.size.height
        val cx = w / 2f
        val cy = h / 2f

        scope.drawRect(Color(0xFF040208), size = scope.size)

        // Core glow
        scope.drawCircle(
            brush = Brush.radialGradient(
                listOf(
                    Color(0xFFFDF4FF).copy(alpha = 0.9f * brightness),
                    Color(0xFFC084FC).copy(alpha = 0.45f * brightness),
                    Color(0xFF6366F1).copy(alpha = 0.2f * brightness),
                    Color.Transparent
                ),
                center = Offset(cx, cy),
                radius = w * 0.35f
            ),
            radius = w * 0.35f,
            center = Offset(cx, cy)
        )

        val count = particles.count.coerceAtMost(90)
        val rotation = time * 0.25f

        for (i in 0 until count) {
            val arm = i % 2
            val armOffset = arm * PI.toFloat()
            val r = (particles.x[i] * 0.45f) * w
            val theta = r * 0.015f + armOffset + rotation + (particles.seed[i] * 0.02f)

            val px = cx + r * cos(theta)
            val py = cy + r * sin(theta) * 0.75f // perspective tilt

            val distanceRatio = (r / (w * 0.45f)).coerceIn(0f, 1f)
            val a = ((1f - distanceRatio * 0.6f) * particles.alpha[i] * brightness).coerceIn(0.1f, 1f)
            val starColor = when {
                arm == 0 -> Color(0xFFA5B4FC)
                else -> Color(0xFFF472B6)
            }

            scope.drawCircle(
                color = starColor.copy(alpha = a),
                radius = (particles.size[i] * 0.6f).coerceIn(1f, 3.5f),
                center = Offset(px, py)
            )
        }
    }

    // 11. BUBBLES
    fun drawBubbles(
        scope: DrawScope,
        particles: BackgroundParticleState,
        time: Float,
        brightness: Float
    ) {
        val w = scope.size.width
        val h = scope.size.height

        scope.drawRect(
            brush = Brush.verticalGradient(
                listOf(Color(0xFF041E26), Color(0xFF083344), Color(0xFF02161E))
            ),
            size = scope.size
        )

        val count = particles.count.coerceAtMost(35)
        for (i in 0 until count) {
            val rise = (particles.y[i] - time * 0.07f * particles.speed[i] + 100f) % 1.0f
            val py = rise * h
            val sway = sin(time * 1.6f + particles.seed[i]) * 18f
            val px = (particles.x[i] * w + sway + w) % w

            val radius = particles.size[i] * 3.5f
            val a = (particles.alpha[i] * 0.6f * brightness).coerceIn(0.15f, 0.8f)

            // Bubble border
            scope.drawCircle(
                color = Color(0xFF5EEAD4).copy(alpha = a),
                radius = radius,
                center = Offset(px, py),
                style = Stroke(width = 1.8f)
            )
            // Specular highlight crescent
            scope.drawCircle(
                color = Color.White.copy(alpha = a * 0.8f),
                radius = radius * 0.28f,
                center = Offset(px - radius * 0.35f, py - radius * 0.35f)
            )
        }
    }

    // 12. DIGITAL MATRIX (Cyber Rain)
    fun drawDigitalMatrix(
        scope: DrawScope,
        particles: BackgroundParticleState,
        time: Float,
        brightness: Float
    ) {
        val w = scope.size.width
        val h = scope.size.height

        scope.drawRect(Color(0xFF030704), size = scope.size)

        val columns = 28
        val colWidth = w / columns

        for (c in 0 until columns) {
            val colSpeed = 0.3f + (particles.speed[c % particles.count] * 0.25f)
            val dropY = ((time * colSpeed + particles.y[c % particles.count]) % 1.2f) * h
            val px = c * colWidth + colWidth / 2f

            val trailLength = h * 0.32f
            val startY = dropY - trailLength

            // Matrix column stream
            scope.drawLine(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color(0xFF15803D).copy(alpha = 0.3f * brightness),
                        Color(0xFF22C55E).copy(alpha = 0.8f * brightness),
                        Color(0xFFDCFCE7).copy(alpha = 0.95f * brightness)
                    ),
                    startY = startY,
                    endY = dropY
                ),
                start = Offset(px, startY),
                end = Offset(px, dropY),
                strokeWidth = 2.5f
            )

            // Leading bright glyph node
            if (dropY in 0f..h) {
                scope.drawCircle(
                    color = Color(0xFFFFFFFF).copy(alpha = 0.9f * brightness),
                    radius = 2.2f,
                    center = Offset(px, dropY)
                )
            }
        }
    }
}
