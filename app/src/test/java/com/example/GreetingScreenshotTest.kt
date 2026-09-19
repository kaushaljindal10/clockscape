package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.graphics.Color
import com.example.clock.ClockStyleType
import com.example.clock.ClockDisplay
import com.example.clock.TimeState
import com.example.data.ClockConfig
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun greeting_screenshot() {
    composeTestRule.setContent {
      MyApplicationTheme(darkTheme = true) {
        ClockDisplay(
          style = ClockStyleType.LARGE_MINIMAL_DIGITAL,
          timeState = TimeState(hour24 = 10, hour12 = 10, minute = 10, second = 0, amPm = "AM", dayOfWeek = "Monday", formattedDate = "Sep 19, 2026"),
          config = ClockConfig(clockColorHex = "#38BDF8"),
          enableBurnInDrift = false
        )
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
  }
}

