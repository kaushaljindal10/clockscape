package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Security
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.StayCurrentLandscape
import androidx.compose.material.icons.filled.StayCurrentPortrait
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ClockConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    config: ClockConfig,
    onToggle24Hour: (Boolean) -> Unit,
    onToggleSeconds: (Boolean) -> Unit,
    onToggleDate: (Boolean) -> Unit,
    onToggleDay: (Boolean) -> Unit,
    onToggleKeepScreenAwake: (Boolean) -> Unit,
    onToggleBurnInProtection: (Boolean) -> Unit,
    onUpdateAnimationSpeed: (Float) -> Unit,
    onUpdateAnimationBrightness: (Float) -> Unit,
    onUpdateClockBrightness: (Float) -> Unit,
    onToggleStartClockAutomatically: (Boolean) -> Unit,
    onToggleDarkMode: (Boolean) -> Unit,
    onUpdateOrientationMode: (String) -> Unit,
    onPreviewSplash: () -> Unit,
    onResetAllSettings: () -> Unit,
    onBack: () -> Unit,
    onUpdateTimerDuration: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    var showResetConfirmation by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("settings_screen"),
        containerColor = Color(0xFF090D16),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF090D16))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Section: Display & Time
            SettingsGroupHeader("DISPLAY & TIME")
            SettingsToggleCard(
                title = "24-Hour Format",
                description = "Display time in 00:00 to 23:59 standard",
                checked = config.is24HourFormat,
                onCheckedChange = onToggle24Hour
            )
            Spacer(Modifier.height(8.dp))
            SettingsToggleCard(
                title = "Show Seconds",
                description = "Display precision seconds indicator",
                checked = config.showSeconds,
                onCheckedChange = onToggleSeconds
            )
            Spacer(Modifier.height(8.dp))
            SettingsToggleCard(
                title = "Show Date",
                description = "Display formatted calendar date",
                checked = config.showDate,
                onCheckedChange = onToggleDate
            )
            Spacer(Modifier.height(8.dp))
            SettingsToggleCard(
                title = "Show Day of Week",
                description = "Display current weekday title",
                checked = config.showDay,
                onCheckedChange = onToggleDay
            )

            Spacer(Modifier.height(24.dp))

            // Section: Screensaver Behavior
            SettingsGroupHeader("SCREENSAVER BEHAVIOR")
            SettingsToggleCard(
                title = "Keep Screen Awake",
                description = "Prevents device from sleeping during Clock Mode",
                checked = config.keepScreenAwake,
                onCheckedChange = onToggleKeepScreenAwake
            )
            Spacer(Modifier.height(8.dp))
            SettingsToggleCard(
                title = "Burn-In Protection",
                description = "Subtly shifts pixel positions over time to protect OLED displays",
                checked = config.burnInProtection,
                onCheckedChange = onToggleBurnInProtection
            )
            Spacer(Modifier.height(8.dp))
            SettingsToggleCard(
                title = "Start Clock Automatically",
                description = "Launch directly into full-screen Clock Mode when app opens",
                checked = config.startClockAutomatically,
                onCheckedChange = onToggleStartClockAutomatically
            )
            Spacer(Modifier.height(8.dp))
            SettingsToggleCard(
                title = "Dark Mode Interface",
                description = "Maintain dark aesthetic across all menus and controls",
                checked = config.isDarkMode,
                onCheckedChange = onToggleDarkMode
            )
            Spacer(Modifier.height(8.dp))
            SettingsOptionCard(
                title = "Screen Orientation",
                description = "Rotate to landscape based on your phone's auto-rotate setting",
                currentValue = config.orientationMode,
                options = listOf(
                    Triple("AUTO", "Auto (Phone)", Icons.Default.ScreenRotation),
                    Triple("LANDSCAPE", "Landscape", Icons.Default.StayCurrentLandscape),
                    Triple("PORTRAIT", "Portrait", Icons.Default.StayCurrentPortrait)
                ),
                onSelect = onUpdateOrientationMode
            )

            Spacer(Modifier.height(24.dp))

            // Section: Visual & Performance Tuning
            SettingsGroupHeader("VISUAL & PERFORMANCE")

            // Animation Speed Preset: Slow, Normal, Fast
            SettingsSpeedPresetCard(
                currentSpeed = config.animationSpeed,
                onSelectSpeed = onUpdateAnimationSpeed
            )
            Spacer(Modifier.height(10.dp))

            // Animation Speed fine slider
            SettingsSliderCard(
                title = "Animation Speed Fine Tuning",
                valueLabel = String.format(java.util.Locale.getDefault(), "%.2fx", config.animationSpeed),
                value = config.animationSpeed,
                range = 0.2f..2.0f,
                onValueChange = onUpdateAnimationSpeed
            )
            Spacer(Modifier.height(10.dp))

            // Animation Brightness
            SettingsSliderCard(
                title = "Animation Brightness",
                valueLabel = "${(config.animationBrightness * 100).toInt()}%",
                value = config.animationBrightness,
                range = 0.2f..1.0f,
                onValueChange = onUpdateAnimationBrightness
            )
            Spacer(Modifier.height(10.dp))

            // Clock Brightness
            SettingsSliderCard(
                title = "Clock Brightness",
                valueLabel = "${(config.clockBrightness * 100).toInt()}%",
                value = config.clockBrightness,
                range = 0.2f..1.0f,
                onValueChange = onUpdateClockBrightness
            )

            Spacer(Modifier.height(24.dp))

            SettingsGroupHeader("APP & DISPLAY")
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131B2A)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPreviewSplash() }
                    .testTag("preview_splash_button")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Replay Splash Screen",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "View the animated Clockscape entrance screen",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color(0xFF60A5FA),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Section: Screen Timer
            SettingsGroupHeader("SCREEN TIMER")
            TimerSectionCard(
                currentDurationMinutes = config.timerDurationMinutes,
                onSelectDuration = onUpdateTimerDuration
            )

            Spacer(Modifier.height(32.dp))

            // Reset All Settings Button
            Button(
                onClick = { showResetConfirmation = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("reset_all_settings_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF261822),
                    contentColor = Color(0xFFF87171)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.RestartAlt,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.size(8.dp))
                Text(
                    text = "Reset All Settings",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }

            Spacer(Modifier.height(36.dp))
        }
    }

    if (showResetConfirmation) {
        AlertDialog(
            onDismissRequest = { showResetConfirmation = false },
            title = { Text("Reset All Settings?") },
            text = {
                Text(
                    "This will restore all background animations, clock styles, colors, and behavior settings back to their factory defaults."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onResetAllSettings()
                        showResetConfirmation = false
                    }
                ) {
                    Text("Reset", color = Color(0xFFEF4444))
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SettingsGroupHeader(title: String) {
    Text(
        text = title,
        color = Color(0xFF64748B),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.3.sp,
        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
    )
}

@Composable
private fun SettingsToggleCard(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131B2A)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = description,
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF3B82F6),
                    uncheckedThumbColor = Color(0xFF94A3B8),
                    uncheckedTrackColor = Color(0xFF1E293B)
                )
            )
        }
    }
}

@Composable
private fun SettingsSliderCard(
    title: String,
    valueLabel: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131B2A)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = valueLabel,
                    color = Color(0xFF60A5FA),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = range,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF3B82F6),
                    activeTrackColor = Color(0xFF3B82F6),
                    inactiveTrackColor = Color(0xFF1E293B)
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun SettingsOptionCard(
    title: String,
    description: String,
    currentValue: String,
    options: List<Triple<String, String, ImageVector>>,
    onSelect: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131B2A)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = description,
                color = Color(0xFF94A3B8),
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                options.forEach { (key, label, icon) ->
                    val isSelected = currentValue == key
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) Color(0xFF3B82F6) else Color(0xFF1E293B),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onSelect(key) }
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else Color(0xFF94A3B8),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = label,
                                color = if (isSelected) Color.White else Color(0xFFCBD5E1),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsSpeedPresetCard(
    currentSpeed: Float,
    onSelectSpeed: (Float) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131B2A)),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Text(
                text = "Animation Speed",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "Lower speed preserves battery life and creates a soothing ambient screensaver",
                color = Color(0xFF94A3B8),
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    Triple(0.5f, "Slow (Default)", "battery-friendly"),
                    Triple(1.0f, "Normal", "balanced"),
                    Triple(1.8f, "Fast", "dynamic")
                ).forEach { (speed, label, sub) ->
                    val isSelected = kotlin.math.abs(currentSpeed - speed) < 0.25f
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) Color(0xFF3B82F6) else Color(0xFF1E293B),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onSelectSpeed(speed) }
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) Color.White else Color(0xFFCBD5E1),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 1
                            )
                            Text(
                                text = sub,
                                color = if (isSelected) Color(0xFFBFDBFE) else Color(0xFF64748B),
                                fontSize = 9.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

