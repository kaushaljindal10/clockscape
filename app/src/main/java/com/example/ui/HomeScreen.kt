package com.example.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.MoreTime
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.background.AnimatedBackground
import com.example.background.BackgroundType
import com.example.clock.ClockDisplay
import com.example.clock.ClockStyleType
import com.example.clock.TimeState
import com.example.clock.rememberCurrentTimeState
import com.example.data.ClockConfig

@Composable
fun HomeScreen(
    config: ClockConfig,
    onStartClockMode: () -> Unit,
    onChooseBackground: () -> Unit,
    onChooseClockStyle: () -> Unit,
    onOpenSettings: () -> Unit,
    onUpdateTimerDuration: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val currentBackground = BackgroundType.fromId(config.selectedBackgroundId)
    val currentClockStyle = ClockStyleType.fromId(config.selectedClockStyleId)
    val timeState = rememberCurrentTimeState(is24Hour = config.is24HourFormat)

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        containerColor = Color(0xFF090D16)
    ) { innerPadding ->
        if (isLandscape) {
            // Adaptive Landscape Dashboard Layout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp, vertical = 10.dp)
            ) {
                // Header (compact for landscape)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Clockscape",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.5).sp
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "Animated screensaver",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF1E293B),
                        modifier = Modifier
                            .size(38.dp)
                            .clickable(onClick = onOpenSettings)
                            .testTag("settings_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = Color(0xFFE2E8F0),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Left Column: Preview + Start Button
                    Column(
                        modifier = Modifier
                            .weight(1.1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .shadow(8.dp, RoundedCornerShape(16.dp))
                                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
                                .clip(RoundedCornerShape(16.dp))
                                .clickable(onClick = onStartClockMode)
                        ) {
                            PreviewCardContent(
                                currentBackground = currentBackground,
                                currentClockStyle = currentClockStyle,
                                timeState = timeState,
                                config = config
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        Button(
                            onClick = onStartClockMode,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .shadow(6.dp, RoundedCornerShape(14.dp))
                                .testTag("start_clock_mode_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF3B82F6),
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "START CLOCK MODE",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    // Right Column: Customization Tiles
                    Column(
                        modifier = Modifier
                            .weight(0.9f)
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CustomizationTile(
                            title = "Background",
                            subtitle = currentBackground.title,
                            icon = Icons.Default.Palette,
                            accentColor = currentBackground.previewAccent,
                            onClick = onChooseBackground,
                            testTag = "choose_background_button"
                        )

                        CustomizationTile(
                            title = "Clock Style",
                            subtitle = "${currentClockStyle.title} • ${config.clockColorName}",
                            icon = Icons.Default.Schedule,
                            accentColor = config.getResolvedClockColor(),
                            onClick = onChooseClockStyle,
                            testTag = "choose_clock_button"
                        )

                        TimerSectionCard(
                            currentDurationMinutes = config.timerDurationMinutes,
                            onSelectDuration = onUpdateTimerDuration
                        )

                        CustomizationTile(
                            title = "Settings & Preferences",
                            subtitle = "Orientation, format, burn-in & speed",
                            icon = Icons.Default.Settings,
                            accentColor = Color(0xFF94A3B8),
                            onClick = onOpenSettings,
                            testTag = "settings_tile_button"
                        )
                    }
                }
            }
        } else {
            // Portrait Layout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // App Title Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Clockscape",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = "Animated screensaver & ambient display",
                            color = Color(0xFF94A3B8),
                            fontSize = 13.sp
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF1E293B),
                        modifier = Modifier
                            .size(44.dp)
                            .clickable(onClick = onOpenSettings)
                            .testTag("settings_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = Color(0xFFE2E8F0),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Live Interactive Preview Card
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 10f)
                        .shadow(12.dp, RoundedCornerShape(20.dp))
                        .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(20.dp))
                        .clip(RoundedCornerShape(20.dp))
                        .clickable(onClick = onStartClockMode)
                ) {
                    PreviewCardContent(
                        currentBackground = currentBackground,
                        currentClockStyle = currentClockStyle,
                        timeState = timeState,
                        config = config
                    )
                }

                Spacer(Modifier.height(24.dp))

                // Primary START CLOCK MODE Button
                Button(
                    onClick = onStartClockMode,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(8.dp, RoundedCornerShape(16.dp))
                        .testTag("start_clock_mode_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3B82F6),
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "START CLOCK MODE",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(Modifier.height(24.dp))

                // Customization Options Group
                Text(
                    text = "CUSTOMIZATION",
                    color = Color(0xFF64748B),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 6.dp)
                )

                // Background Selection Card
                CustomizationTile(
                    title = "Background",
                    subtitle = currentBackground.title,
                    icon = Icons.Default.Palette,
                    accentColor = currentBackground.previewAccent,
                    onClick = onChooseBackground,
                    testTag = "choose_background_button"
                )

                Spacer(Modifier.height(10.dp))

                // Clock Style Selection Card
                CustomizationTile(
                    title = "Clock Style",
                    subtitle = "${currentClockStyle.title} • ${config.clockColorName}",
                    icon = Icons.Default.Schedule,
                    accentColor = config.getResolvedClockColor(),
                    onClick = onChooseClockStyle,
                    testTag = "choose_clock_button"
                )

                Spacer(Modifier.height(10.dp))

                // Timer Section Card (Feature 1)
                TimerSectionCard(
                    currentDurationMinutes = config.timerDurationMinutes,
                    onSelectDuration = onUpdateTimerDuration
                )

                Spacer(Modifier.height(10.dp))

                // Settings Card
                CustomizationTile(
                    title = "Settings & Preferences",
                    subtitle = "Orientation, format, burn-in & speed",
                    icon = Icons.Default.Settings,
                    accentColor = Color(0xFF94A3B8),
                    onClick = onOpenSettings,
                    testTag = "settings_tile_button"
                )

                Spacer(Modifier.height(28.dp))
            }
        }
    }
}

@Composable
private fun PreviewCardContent(
    currentBackground: BackgroundType,
    currentClockStyle: ClockStyleType,
    timeState: TimeState,
    config: ClockConfig
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Background layer
        AnimatedBackground(
            type = currentBackground,
            speed = config.animationSpeed,
            brightness = config.animationBrightness,
            customImagePath = config.customImagePath
        )

        // Clock overlay
        ClockDisplay(
            style = currentClockStyle,
            timeState = timeState,
            config = config.copy(clockSize = config.clockSize * 0.72f),
            enableBurnInDrift = false
        )

        // Top-right "Live Preview" badge
        Surface(
            color = Color(0x990F172A),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(Color(0xFF22C55E), CircleShape)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "LIVE PREVIEW",
                    color = Color(0xFFE2E8F0),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        // Bottom subtle tap hint
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
                .background(Color(0x88000000), RoundedCornerShape(10.dp))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text(
                text = "Tap to enter full-screen",
                color = Color(0xCCFFFFFF),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun CustomizationTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131B2A)),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(accentColor.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                        .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        color = Color(0xFF94A3B8),
                        fontSize = 13.sp
                    )
                }
            }

            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF64748B),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun TimerSectionCard(
    currentDurationMinutes: Int,
    onSelectDuration: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCustomDialog by remember { mutableStateOf(false) }

    val durationPresets = listOf(
        0 to "Continuous",
        5 to "5m",
        15 to "15m",
        30 to "30m",
        60 to "1h",
        120 to "2h",
        300 to "5h",
        600 to "10h"
    )

    val currentTitle = when (currentDurationMinutes) {
        0 -> "Continuous (No Timer)"
        5 -> "5 Minutes"
        15 -> "15 Minutes"
        30 -> "30 Minutes"
        60 -> "1 Hour"
        120 -> "2 Hours"
        300 -> "5 Hours"
        600 -> "10 Hours"
        else -> {
            val h = currentDurationMinutes / 60
            val m = currentDurationMinutes % 60
            if (h > 0 && m > 0) "${h}h ${m}m"
            else if (h > 0) "${h} Hours"
            else "${m} Minutes"
        }
    }

    val subtitle = if (currentDurationMinutes == 0) {
        "Clock stays active indefinitely until tapped"
    } else {
        "Auto-unlocks and exits after $currentTitle"
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF141C2E)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("timer_section_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0x2638BDF8), RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0x5938BDF8), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.HourglassTop,
                            contentDescription = "Timer",
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(Modifier.width(14.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Display Timer",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (currentDurationMinutes > 0) Color(0x3338BDF8) else Color(0x3364748B)
                            ) {
                                Text(
                                    text = if (currentDurationMinutes > 0) currentTitle else "Off",
                                    color = if (currentDurationMinutes > 0) Color(0xFF38BDF8) else Color(0xFF94A3B8),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = subtitle,
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // Preset Chips Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                durationPresets.forEach { (mins, label) ->
                    val isSelected = currentDurationMinutes == mins
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) Color(0xFF2563EB) else Color(0xFF1E293B),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) Color(0xFF60A5FA) else Color(0xFF334155)
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onSelectDuration(mins) }
                            .testTag("timer_preset_${mins}m")
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) Color.White else Color(0xFFCBD5E1),
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }

                // Custom Duration Chip
                val isCustomSelected = durationPresets.none { it.first == currentDurationMinutes }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isCustomSelected) Color(0xFF2563EB) else Color(0xFF1E293B),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isCustomSelected) Color(0xFF60A5FA) else Color(0xFF334155)
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { showCustomDialog = true }
                        .testTag("timer_custom_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreTime,
                            contentDescription = "Custom duration",
                            tint = if (isCustomSelected) Color.White else Color(0xFF94A3B8),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = if (isCustomSelected) currentTitle else "Custom...",
                            color = if (isCustomSelected) Color.White else Color(0xFFCBD5E1),
                            fontSize = 13.sp,
                            fontWeight = if (isCustomSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }
    }

    if (showCustomDialog) {
        CustomDurationDialog(
            initialMinutes = if (currentDurationMinutes > 0) currentDurationMinutes else 45,
            onDismiss = { showCustomDialog = false },
            onConfirm = { mins ->
                onSelectDuration(mins)
                showCustomDialog = false
            }
        )
    }
}

@Composable
fun CustomDurationDialog(
    initialMinutes: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var hours by remember { mutableIntStateOf(initialMinutes / 60) }
    var minutes by remember { mutableIntStateOf(initialMinutes % 60) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Set Custom Timer",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Screen will stay active for this duration before automatically unlocking and returning home.",
                    color = Color(0xFF94A3B8),
                    fontSize = 13.sp
                )

                // Hours Slider
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Hours", color = Color(0xFFE2E8F0), fontSize = 14.sp)
                        Text(text = "$hours h", color = Color(0xFF38BDF8), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Slider(
                        value = hours.toFloat(),
                        onValueChange = { hours = it.toInt() },
                        valueRange = 0f..12f,
                        steps = 11,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF38BDF8),
                            activeTrackColor = Color(0xFF38BDF8),
                            inactiveTrackColor = Color(0xFF334155)
                        )
                    )
                }

                // Minutes Slider
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Minutes", color = Color(0xFFE2E8F0), fontSize = 14.sp)
                        Text(text = "$minutes min", color = Color(0xFF38BDF8), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Slider(
                        value = minutes.toFloat(),
                        onValueChange = { minutes = it.toInt() },
                        valueRange = 0f..55f,
                        steps = 10,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF38BDF8),
                            activeTrackColor = Color(0xFF38BDF8),
                            inactiveTrackColor = Color(0xFF334155)
                        )
                    )
                }

                val totalMin = hours * 60 + minutes
                val previewStr = if (totalMin <= 0) "Continuous (Off)" else if (hours > 0 && minutes > 0) "${hours}h ${minutes}m" else if (hours > 0) "${hours} Hours" else "${minutes} Minutes"

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF0F172A),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Total Duration: $previewStr",
                        color = Color(0xFF60A5FA),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val total = hours * 60 + minutes
                    onConfirm(total)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
            ) {
                Text("Set Timer")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFF94A3B8))
            }
        },
        containerColor = Color(0xFF1E293B),
        shape = RoundedCornerShape(20.dp)
    )
}

