package com.example.steamtracker.ui

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.pressKey
import com.example.steamtracker.ui.theme.SteamTrackerTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NewsScreenTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalTestApi::class)
    @Before
    fun setup() {
        // Set up the User interface for tests
        composeTestRule.setContent {
            SteamTrackerTheme {
                SteamTrackerApp()
            }
        }

        // Search for Split Fiction
        composeTestRule.onNodeWithText("Search the Steam Store")
            .performClick()
            .performTextInput("Split Fiction")
        composeTestRule.onNodeWithText("Split Fiction")
            .performKeyInput {
                pressKey(Key.Enter)
            }
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("SearchResultsList")
        )
        composeTestRule.onNodeWithText("Split Fiction")
            .performClick()

        // Wait until App Page Loads
        composeTestRule.waitUntilExactlyOneExists(
            hasText("Track News")
        )

        // Add game to news list
        composeTestRule.onNodeWithText("Track News")
            .performClick()

        // Switch to news screen
        composeTestRule.onNodeWithContentDescription("News")
            .performClick()

        // Wait for news to show
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("NewsList")
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun newsCard_verifyNewsDetailsListShows() {
        // Scroll in list and select news post
        composeTestRule.onNodeWithTag("NewsList")
            .performScrollToIndex(4)
            .performClick()

        // Verify news details exist
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("NewsDetailsList")
        )
    }
}
