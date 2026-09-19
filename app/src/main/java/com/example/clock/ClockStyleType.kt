package com.example.clock

enum class ClockStyleType(
    val id: String,
    val title: String,
    val subtitle: String,
    val isAnalog: Boolean = false
) {
    LARGE_MINIMAL_DIGITAL(
        id = "LARGE_MINIMAL_DIGITAL",
        title = "Large Minimal Digital",
        subtitle = "Clean, bold display with high visual presence"
    ),
    THIN_ELEGANT_DIGITAL(
        id = "THIN_ELEGANT_DIGITAL",
        title = "Thin Elegant Digital",
        subtitle = "Refined ultra-light typography and luxury tracking"
    ),
    NEON_DIGITAL(
        id = "NEON_DIGITAL",
        title = "Neon Digital",
        subtitle = "Luminous multi-layered neon glow tubes"
    ),
    RETRO_DIGITAL(
        id = "RETRO_DIGITAL",
        title = "Retro Digital",
        subtitle = "Classic vintage 7-segment digital alarm clock"
    ),
    FLIP_CLOCK(
        id = "FLIP_CLOCK",
        title = "Flip Clock",
        subtitle = "Split-flap retro mechanical card display"
    ),
    LARGE_ANALOG(
        id = "LARGE_ANALOG",
        title = "Large Analog",
        subtitle = "Full-face dial with precision minute markers",
        isAnalog = true
    ),
    MINIMAL_ANALOG(
        id = "MINIMAL_ANALOG",
        title = "Minimal Analog",
        subtitle = "Subtle hour indices with sleek baton hands",
        isAnalog = true
    ),
    ANALOG_DIGITAL(
        id = "ANALOG_DIGITAL",
        title = "Analog + Digital",
        subtitle = "Dual hybrid layout with synchronized dial & digits",
        isAnalog = true
    ),
    DOT_MATRIX(
        id = "DOT_MATRIX",
        title = "Dot Matrix",
        subtitle = "Engineered LED dot matrix grid typography"
    ),
    FUTURISTIC_DIGITAL(
        id = "FUTURISTIC_DIGITAL",
        title = "Futuristic Digital",
        subtitle = "Cyberpunk HUD frame with tech telemetry accents"
    );

    companion object {
        fun fromId(id: String): ClockStyleType {
            return entries.find { it.id.equals(id, ignoreCase = true) } ?: LARGE_MINIMAL_DIGITAL
        }
    }
}
