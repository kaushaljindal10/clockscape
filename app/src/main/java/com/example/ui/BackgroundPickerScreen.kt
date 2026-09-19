package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.background.AnimatedBackground
import com.example.background.BackgroundType
import com.example.clock.ClockDisplay
import com.example.clock.ClockStyleType
import com.example.clock.rememberCurrentTimeState
import com.example.data.ClockConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackgroundPickerScreen(
    config: ClockConfig,
    onSelectBackground: (BackgroundType) -> Unit,
    onUpdateSpeed: ((Float) -> Unit)? = null,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val currentSelectedBg = remember(config.selectedBackgroundId) {
        BackgroundType.fromId(config.selectedBackgroundId)
    }

    val currentClockStyle = remember(config.selectedClockStyleId) {
        ClockStyleType.fromId(config.selectedClockStyleId)
    }

    val timeState = rememberCurrentTimeState(is24Hour = config.is24HourFormat)
    val backgroundList = remember { BackgroundType.entries }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("background_picker_screen"),
        containerColor = Color(0xFF090D16),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Choose Background",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Ambient screensavers & dynamic themes",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp
                        )
                    }
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Hero Live Preview with Active Clock Layer
            item(span = { GridItemSpan(2) }) {
                HeroLivePreviewCard(
                    background = currentSelectedBg,
                    clockStyle = currentClockStyle,
                    timeState = timeState,
                    config = config,
                    onUpdateSpeed = onUpdateSpeed
                )
            }

            // Grid of Background Cards
            items(backgroundList, key = { it.id }) { bg ->
                val isSelected = bg.id.equals(config.selectedBackgroundId, ignoreCase = true)
                BackgroundPreviewCard(
                    background = bg,
                    isSelected = isSelected,
                    speed = config.animationSpeed,
                    brightness = config.animationBrightness,
                    onClick = { onSelectBackground(bg) }
                )
            }

            item(span = { GridItemSpan(2) }) {
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

/**
 * Hero preview card showing the active background running live with the user's clock overlaid.
 */
@Composable
private fun HeroLivePreviewCard(
    background: BackgroundType,
    clockStyle: ClockStyleType,
    timeState: com.example.clock.TimeState,
    config: ClockConfig,
    onUpdateSpeed: ((Float) -> Unit)?,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131B2A)),
        modifier = modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(20.dp))
            .border(1.5.dp, Color(0xFF1E293B), RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .testTag("hero_preview_card")
    ) {
        Column {
            // Live Screensaver Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            ) {
                // Background animation layer
                AnimatedBackground(
                    type = background,
                    speed = config.animationSpeed,
                    brightness = config.animationBrightness
                )

                // Overlying Clock layer
                ClockDisplay(
                    style = clockStyle,
                    timeState = timeState,
                    config = config,
                    enableBurnInDrift = false
                )

                // Live Preview Tag
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .background(Color(0x990F172A), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "LIVE CLOCK PREVIEW",
                        color = Color(0xFF38BDF8),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Controls & Background Metadata
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
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = background.title,
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = background.description,
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Speed Selector: Slow / Normal / Fast
                if (onUpdateSpeed != null) {
                    Spacer(Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = null,
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Speed:",
                                color = Color(0xFF94A3B8),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(
                                Triple("Slow", 0.5f, "speed_slow"),
                                Triple("Normal", 1.0f, "speed_normal"),
                                Triple("Fast", 1.8f, "speed_fast")
                            ).forEach { (label, speedVal, tag) ->
                                val isSelected = kotlin.math.abs(config.animationSpeed - speedVal) < 0.25f
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) Color(0xFF3B82F6) else Color(0xFF1E293B),
                                    modifier = Modifier
                                        .clickable { onUpdateSpeed(speedVal) }
                                        .testTag(tag)
                                        .clip(RoundedCornerShape(8.dp))
                                ) {
                                    Text(
                                        text = label,
                                        color = if (isSelected) Color.White else Color(0xFFCBD5E1),
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Selectable background card with live miniature canvas animation.
 */
@Composable
private fun BackgroundPreviewCard(
    background: BackgroundType,
    isSelected: Boolean,
    speed: Float,
    brightness: Float,
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
            .testTag("bg_card_${background.id.lowercase()}")
    ) {
        Column {
            // Live Mini Animated Background Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                AnimatedBackground(
                    type = background,
                    speed = speed,
                    brightness = brightness
                )

                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(26.dp)
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

            // Text Info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = background.title,
                    color = if (isSelected) Color(0xFF60A5FA) else Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = background.description,
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
