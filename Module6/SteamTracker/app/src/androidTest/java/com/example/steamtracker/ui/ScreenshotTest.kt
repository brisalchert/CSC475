package com.example.steamtracker.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import com.example.steamtracker.ui.theme.SteamTrackerTheme
import org.junit.Rule
import org.junit.Test

class ScreenshotTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun screenshot_verifyFullscreenScreenshotAndBackButton() {
        composeTestRule.setContent {
            SteamTrackerTheme {
                SteamTrackerApp()
            }
        }

        // Wait for the Recommended tab to load
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("FeaturedGamesList")
        )

        composeTestRule.onNodeWithTag("FeaturedGamesList")
            .performScrollToIndex(1)
            .performClick()

        // Verify App Page text exists
        composeTestRule.waitUntilExactlyOneExists(
            hasText("SCREENSHOTS")
        )

        // Scroll and click on screenshot
        composeTestRule.onNodeWithTag("ScreenshotsRow")
            .performScrollToIndex(3)
            .performClick()

        // Verify screenshot page exists
        composeTestRule.waitUntilExactlyOneExists(
            hasContentDescription("Fullscreen screenshot")
        )

        // Press back arrow and verify on App Page
        composeTestRule.onNodeWithContentDescription("Back Button")
            .performClick()

        composeTestRule.waitUntilExactlyOneExists(
            hasText("SCREENSHOTS")
        )
    }
}
