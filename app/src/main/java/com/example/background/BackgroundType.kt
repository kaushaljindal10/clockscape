package com.example.background

import androidx.compose.ui.graphics.Color

enum class BackgroundType(
    val id: String,
    val title: String,
    val description: String,
    val previewAccent: Color
) {
    AURORA(
        id = "AURORA",
        title = "Aurora",
        description = "Flowing ribbons of polar emerald, teal, and violet lights",
        previewAccent = Color(0xFF10B981)
    ),
    STARFIELD(
        id = "STARFIELD",
        title = "Starfield",
        description = "Deep cosmos with twinkling stars and drifting stardust",
        previewAccent = Color(0xFF60A5FA)
    ),
    FALLING_SNOW(
        id = "FALLING_SNOW",
        title = "Falling Snow",
        description = "Gentle winter snowfall drifting through serene night air",
        previewAccent = Color(0xFFE2E8F0)
    ),
    RAIN(
        id = "RAIN",
        title = "Rain",
        description = "Streaking cinematic raindrops with gentle floor splashes",
        previewAccent = Color(0xFF38BDF8)
    ),
    OCEAN_WAVES(
        id = "OCEAN_WAVES",
        title = "Ocean Waves",
        description = "Calm undulating layered tides in deep marine indigo",
        previewAccent = Color(0xFF0284C7)
    ),
    FIRE_FLAMES(
        id = "FIRE_FLAMES",
        title = "Fire / Flames",
        description = "Warm rising heat embers and flickering flame gradient",
        previewAccent = Color(0xFFF97316)
    ),
    FLOATING_PARTICLES(
        id = "FLOATING_PARTICLES",
        title = "Floating Particles",
        description = "Luminous glowing bokeh orbs drifting gracefully",
        previewAccent = Color(0xFFA855F7)
    ),
    COLOR_GRADIENT(
        id = "COLOR_GRADIENT",
        title = "Color Gradient",
        description = "Hypnotic slowly evolving multi-hue liquid gradient",
        previewAccent = Color(0xFFEC4899)
    ),
    NEON_FLUID(
        id = "NEON_FLUID",
        title = "Neon Fluid",
        description = "Electrifying magenta and cyan fluid plasma ribbons",
        previewAccent = Color(0xFF06B6D4)
    ),
    GALAXY(
        id = "GALAXY",
        title = "Galaxy",
        description = "Slowly rotating spiral galaxy with radiant cosmic core",
        previewAccent = Color(0xFF818CF8)
    ),
    BUBBLES(
        id = "BUBBLES",
        title = "Bubbles",
        description = "Iridescent translucent spheres rising through deep water",
        previewAccent = Color(0xFF2DD4BF)
    ),
    DIGITAL_MATRIX(
        id = "DIGITAL_MATRIX",
        title = "Digital Matrix",
        description = "Vertical cascading cybernetic code rain in matrix green",
        previewAccent = Color(0xFF22C55E)
    );

    companion object {
        fun fromId(id: String): BackgroundType {
            return entries.find {
                it.id.equals(id, ignoreCase = true) ||
                it.name.equals(id, ignoreCase = true)
            } ?: AURORA
        }
    }
}
