package com.example.steamtracker.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import com.example.steamtracker.ui.theme.SteamTrackerTheme
import org.junit.Rule
import org.junit.Test

class SalesGameTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun salesGame_verifyAppPageShows() {
        composeTestRule.setContent {
            SteamTrackerTheme {
                SteamTrackerApp()
            }
        }

        composeTestRule.waitUntilExactlyOneExists(
            hasText("On Sale"),
            timeoutMillis = 5000
        )

        composeTestRule.onNodeWithText("On Sale").performClick()

        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("SalesGamesList"),
            timeoutMillis = 5000
        )

        composeTestRule.onNodeWithTag("SalesGamesList")
            .performScrollToIndex(5)
            .performClick()

        composeTestRule.waitUntilExactlyOneExists(
            hasText("SCREENSHOTS"),
            timeoutMillis = 5000
        )
    }
}
