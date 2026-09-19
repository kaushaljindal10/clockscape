package com.example.data

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clock_config")
data class ClockConfig(
    @PrimaryKey val id: Int = 1,
    val selectedBackgroundId: String = "AURORA",
    val selectedClockStyleId: String = "LARGE_MINIMAL_DIGITAL",
    val clockColorHex: String = "#FFFFFF",
    val clockColorName: String = "White",
    val clockSize: Float = 1.0f, // 0.7f to 1.3f
    val clockPosition: String = "Center", // "Top", "Center", "Bottom"
    val clockOpacity: Float = 1.0f, // 0.2f to 1.0f
    val showSeconds: Boolean = true,
    val showDate: Boolean = true,
    val showDay: Boolean = true,
    val is24HourFormat: Boolean = false,
    val keepScreenAwake: Boolean = true,
    val burnInProtection: Boolean = true,
    val animationSpeed: Float = 0.5f, // Slow (0.5f) by default
    val animationBrightness: Float = 1.0f, // 0.2f to 1.0f
    val clockBrightness: Float = 1.0f, // 0.2f to 1.0f
    val startClockAutomatically: Boolean = false,
    val isDarkMode: Boolean = true,
    val orientationMode: String = "AUTO", // "AUTO" (Phone Auto-Rotate), "LANDSCAPE", "PORTRAIT"
    val timerDurationMinutes: Int = 0, // 0 = continuous/off, or 5, 15, 30, 60 (1h), 120 (2h), 600 (10h), etc.
    val customImagePath: String? = null // Path to imported exact image file if selected
) {
    fun getResolvedClockColor(isBackgroundLight: Boolean = false): Color {
        if (clockColorName == "High-Contrast") {
            return if (isBackgroundLight) Color(0xFF0F172A) else Color(0xFFF8FAFC)
        }
        return try {
            val cleanHex = clockColorHex.removePrefix("#")
            val colorLong = cleanHex.toLong(16)
            if (cleanHex.length == 6) {
                Color(0xFF000000 or colorLong)
            } else if (cleanHex.length == 8) {
                Color(colorLong)
            } else {
                Color.White
            }
        } catch (_: Exception) {
            Color.White
        }
    }

    companion object {
        val COLOR_PRESETS = listOf(
            ClockColorOption("White", "#FFFFFF", Color(0xFFFFFFFF)),
            ClockColorOption("High-Contrast", "AUTO", Color(0xFFE2E8F0)),
            ClockColorOption("Cyan", "#38BDF8", Color(0xFF38BDF8)),
            ClockColorOption("Green", "#4ADE80", Color(0xFF4ADE80)),
            ClockColorOption("Purple", "#C084FC", Color(0xFFC084FC)),
            ClockColorOption("Orange", "#FB923C", Color(0xFFFB923C)),
            ClockColorOption("Red", "#F87171", Color(0xFFF87171)),
            ClockColorOption("Yellow", "#FDE047", Color(0xFFFDE047)),
            ClockColorOption("Blue", "#60A5FA", Color(0xFF60A5FA)),
            ClockColorOption("Black", "#000000", Color(0xFF1E293B))
        )
    }
}

data class ClockColorOption(
    val name: String,
    val hex: String,
    val color: Color
)
