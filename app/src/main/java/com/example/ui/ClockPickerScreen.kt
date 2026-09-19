package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clock.ClockDisplay
import com.example.clock.ClockStyleType
import com.example.clock.rememberCurrentTimeState
import com.example.data.ClockColorOption
import com.example.data.ClockConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClockPickerScreen(
    config: ClockConfig,
    onSelectClockStyle: (ClockStyleType) -> Unit,
    onSelectColor: (name: String, hex: String) -> Unit,
    onUpdateSize: (Float) -> Unit,
    onUpdatePosition: (String) -> Unit,
    onUpdateOpacity: (Float) -> Unit,
    onToggleSeconds: (Boolean) -> Unit,
    onToggleDate: (Boolean) -> Unit,
    onToggleDay: (Boolean) -> Unit,
    onToggle24Hour: (Boolean) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    var selectedTab by remember { mutableIntStateOf(0) }
    var showCustomColorDialog by remember { mutableStateOf(false) }
    val timeState = rememberCurrentTimeState(is24Hour = config.is24HourFormat)

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("clock_picker_screen"),
        containerColor = Color(0xFF090D16),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Customize Clock",
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
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFF090D16),
                contentColor = Color(0xFF3B82F6),
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Color(0xFF3B82F6)
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Clock Styles", fontWeight = FontWeight.SemiBold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Color & Layout", fontWeight = FontWeight.SemiBold) }
                )
            }

            if (selectedTab == 0) {
                // List of 10 clock styles with live previews
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(ClockStyleType.entries) { style ->
                        val isSelected = style.id.equals(config.selectedClockStyleId, ignoreCase = true)
                        ClockStyleCard(
                            style = style,
                            isSelected = isSelected,
                            timeState = timeState,
                            config = config,
                            onClick = { onSelectClockStyle(style) }
                        )
                    }
                }
            } else {
                // Appearance & Layout Customization
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp)
                ) {
                    // Color Picker Section
                    SectionHeader("CLOCK COLOR")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ClockConfig.COLOR_PRESETS.forEach { colorOpt ->
                            val isColorSelected = config.clockColorName == colorOpt.name
                            ColorChip(
                                option = colorOpt,
                                isSelected = isColorSelected,
                                onClick = { onSelectColor(colorOpt.name, colorOpt.hex) }
                            )
                        }

                        // Custom Color Button
                        val isCustom = config.clockColorName == "Custom"
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1E293B))
                                .border(
                                    width = if (isCustom) 2.5.dp else 1.dp,
                                    color = if (isCustom) Color(0xFF3B82F6) else Color(0xFF334155),
                                    shape = CircleShape
                                )
                                .clickable { showCustomColorDialog = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "HEX",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // Clock Size Slider
                    SectionHeader("CLOCK SIZE (${(config.clockSize * 100).toInt()}%)")
                    Slider(
                        value = config.clockSize,
                        onValueChange = onUpdateSize,
                        valueRange = 0.7f..1.3f,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF3B82F6),
                            activeTrackColor = Color(0xFF3B82F6),
                            inactiveTrackColor = Color(0xFF1E293B)
                        )
                    )

                    Spacer(Modifier.height(20.dp))

                    // Clock Position Selector
                    SectionHeader("CLOCK POSITION")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf("Top", "Center", "Bottom").forEach { pos ->
                            val isPosSelected = config.clockPosition.equals(pos, ignoreCase = true)
                            FilterChip(
                                selected = isPosSelected,
                                onClick = { onUpdatePosition(pos) },
                                label = { Text(pos, fontWeight = FontWeight.Medium) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF3B82F6),
                                    selectedLabelColor = Color.White,
                                    containerColor = Color(0xFF131B2A),
                                    labelColor = Color(0xFF94A3B8)
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // Clock Opacity Slider
                    SectionHeader("CLOCK OPACITY (${(config.clockOpacity * 100).toInt()}%)")
                    Slider(
                        value = config.clockOpacity,
                        onValueChange = onUpdateOpacity,
                        valueRange = 0.2f..1.0f,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF3B82F6),
                            activeTrackColor = Color(0xFF3B82F6),
                            inactiveTrackColor = Color(0xFF1E293B)
                        )
                    )

                    Spacer(Modifier.height(24.dp))

                    // Display Elements Toggles
                    SectionHeader("DISPLAY ELEMENTS")
                    ToggleRow(
                        title = "Show Seconds",
                        checked = config.showSeconds,
                        onCheckedChange = onToggleSeconds
                    )
                    Spacer(Modifier.height(10.dp))
                    ToggleRow(
                        title = "Show Date",
                        checked = config.showDate,
                        onCheckedChange = onToggleDate
                    )
                    Spacer(Modifier.height(10.dp))
                    ToggleRow(
                        title = "Show Day of Week",
                        checked = config.showDay,
                        onCheckedChange = onToggleDay
                    )
                    Spacer(Modifier.height(10.dp))
                    ToggleRow(
                        title = "24-Hour Format",
                        checked = config.is24HourFormat,
                        onCheckedChange = onToggle24Hour
                    )

                    Spacer(Modifier.height(32.dp))
                }
            }
        }
    }

    if (showCustomColorDialog) {
        var hexInput by remember { mutableStateOf(config.clockColorHex.removePrefix("#")) }
        AlertDialog(
            onDismissRequest = { showCustomColorDialog = false },
            title = { Text("Custom Clock Color") },
            text = {
                Column {
                    Text(
                        text = "Enter a 6-digit hexadecimal color code (e.g. FF5722, 00E5FF):",
                        color = Color(0xFF94A3B8),
                        fontSize = 13.sp
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = hexInput,
                        onValueChange = { if (it.length <= 6) hexInput = it.uppercase() },
                        prefix = { Text("#") },
                        singleLine = true,
                        placeholder = { Text("38BDF8") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val finalHex = "#$hexInput"
                        onSelectColor("Custom", finalHex)
                        showCustomColorDialog = false
                    }
                ) {
                    Text("Apply Color")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomColorDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ClockStyleCard(
    style: ClockStyleType,
    isSelected: Boolean,
    timeState: com.example.clock.TimeState,
    config: ClockConfig,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Color(0xFF3B82F6) else Color(0xFF1E293B)
    val borderWidth = if (isSelected) 2.5.dp else 1.dp

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131B2A)),
        modifier = Modifier
            .fillMaxWidth()
            .border(borderWidth, borderColor, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("clock_card_${style.id.lowercase()}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = style.title,
                        color = if (isSelected) Color(0xFF60A5FA) else Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = style.subtitle,
                        color = Color(0xFF94A3B8),
                        fontSize = 12.sp
                    )
                }

                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color(0xFF3B82F6), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // Live preview container of this clock style
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF090D16))
                    .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                ClockDisplay(
                    style = style,
                    timeState = timeState,
                    config = config.copy(
                        clockSize = 0.52f,
                        clockPosition = "Center",
                        showDate = false,
                        showDay = false
                    ),
                    enableBurnInDrift = false
                )
            }
        }
    }
}

@Composable
private fun ColorChip(
    option: ClockColorOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(option.color)
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) Color(0xFF3B82F6) else Color(0x44FFFFFF),
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = if (option.name == "White" || option.name == "Yellow") Color.Black else Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        color = Color(0xFF64748B),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun ToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131B2A)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
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
