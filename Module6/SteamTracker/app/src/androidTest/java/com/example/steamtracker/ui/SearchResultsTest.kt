package com.example.steamtracker.ui

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.pressKey
import com.example.steamtracker.ui.theme.SteamTrackerTheme
import org.junit.Rule
import org.junit.Test

class SearchResultsTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun searchResults_verifyResults() {
        composeTestRule.setContent {
            SteamTrackerTheme {
                SteamTrackerApp()
            }
        }

        // Search for Dark Souls
        composeTestRule.onNodeWithText("Search the Steam Store")
            .performClick()
            .performTextInput("Dark Souls")
        composeTestRule.onNodeWithText("Dark Souls")
            .performKeyInput {
                pressKey(Key.Enter)
            }

        // Verify results exist
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("SearchResultsList")
        )

        // Check for expected results
        composeTestRule.onNodeWithText("DARK SOULS™ III").assertExists()
        composeTestRule.onNodeWithText("DARK SOULS™: REMASTERED").assertExists()
        composeTestRule.onNodeWithText("DARK SOULS™ II").assertExists()
        composeTestRule.onNodeWithText("DARK SOULS™ III - The Ringed City™").assertExists()

        // Select Dark Souls III
        composeTestRule.onNodeWithText("DARK SOULS™ III")
            .performClick()

        // Verify app page exists
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("AppPageList")
        )
    }
}
