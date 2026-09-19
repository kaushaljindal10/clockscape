package com.example.clock

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class TimeState(
    val hour24: Int = 0,
    val hour12: Int = 12,
    val minute: Int = 0,
    val second: Int = 0,
    val millisecond: Int = 0,
    val is24Hour: Boolean = false,
    val amPm: String = "AM",
    val dayOfWeek: String = "",
    val dayOfWeekShort: String = "",
    val formattedDate: String = "",
    val formattedDateShort: String = ""
) {
    val displayHour: Int get() = if (is24Hour) hour24 else hour12
    val hourString: String get() = String.format(Locale.getDefault(), "%02d", displayHour)
    val minuteString: String get() = String.format(Locale.getDefault(), "%02d", minute)
    val secondString: String get() = String.format(Locale.getDefault(), "%02d", second)

    // Analog hand angles (in degrees, 0 = 12 o'clock)
    val hourAngle: Float
        get() = ((hour24 % 12) + minute / 60f + second / 3600f) * 30f

    val minuteAngle: Float
        get() = (minute + second / 60f) * 6f

    val secondAngle: Float
        get() = (second + millisecond / 1000f) * 6f

    companion object {
        fun current(is24Hour: Boolean = false): TimeState {
            val calendar = Calendar.getInstance()
            val hour24 = calendar.get(Calendar.HOUR_OF_DAY)
            val rawHour12 = calendar.get(Calendar.HOUR)
            val hour12 = if (rawHour12 == 0) 12 else rawHour12
            val minute = calendar.get(Calendar.MINUTE)
            val second = calendar.get(Calendar.SECOND)
            val millisecond = calendar.get(Calendar.MILLISECOND)
            val amPm = if (calendar.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"

            val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
            val dayShortFormat = SimpleDateFormat("EEE", Locale.getDefault())
            val dateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
            val dateShortFormat = SimpleDateFormat("MMM d", Locale.getDefault())

            val date = calendar.time
            return TimeState(
                hour24 = hour24,
                hour12 = hour12,
                minute = minute,
                second = second,
                millisecond = millisecond,
                is24Hour = is24Hour,
                amPm = amPm,
                dayOfWeek = dayFormat.format(date),
                dayOfWeekShort = dayShortFormat.format(date).uppercase(Locale.getDefault()),
                formattedDate = dateFormat.format(date),
                formattedDateShort = dateShortFormat.format(date)
            )
        }
    }
}

@Composable
fun rememberCurrentTimeState(is24Hour: Boolean = false, updateIntervalMs: Long = 50L): TimeState {
    var state by remember(is24Hour) { mutableStateOf(TimeState.current(is24Hour)) }

    LaunchedEffect(is24Hour, updateIntervalMs) {
        while (isActive) {
            state = TimeState.current(is24Hour)
            delay(updateIntervalMs)
        }
    }

    return state
}
