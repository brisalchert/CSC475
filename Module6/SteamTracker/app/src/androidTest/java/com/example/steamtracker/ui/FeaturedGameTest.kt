package com.example.steamtracker.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import com.example.steamtracker.ui.theme.SteamTrackerTheme
import org.junit.Rule
import org.junit.Test

class FeaturedGameTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun myTest() {
        composeTestRule.setContent {
            SteamTrackerTheme {
                SteamTrackerApp()
            }
        }

        val index = 3

        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("FeaturedGamesList"),
            timeoutMillis = 5000
        )

        composeTestRule.onNodeWithTag("FeaturedGamesList")
            .performScrollToIndex(index).performClick()

        composeTestRule.waitUntilExactlyOneExists(
            hasText("SCREENSHOTS"),
            timeoutMillis = 5000
        )
    }
}
