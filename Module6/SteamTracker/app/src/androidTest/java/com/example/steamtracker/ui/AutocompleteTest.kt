package com.example.steamtracker.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.steamtracker.ui.theme.SteamTrackerTheme
import org.junit.Rule
import org.junit.Test

class AutocompleteTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun autocompleteResults_verifyAccessAppPage() {
        composeTestRule.setContent {
            SteamTrackerTheme {
                SteamTrackerApp()
            }
        }

        // Search for Dark Souls III
        composeTestRule.onNodeWithText("Search the Steam Store")
            .performClick()
            .performTextInput("Dark Souls")

        // Select autocomplete result
        composeTestRule.waitUntilExactlyOneExists(
            hasText("DARK SOULS™ III"),
            timeoutMillis = 3000
        )
        composeTestRule.onNodeWithText("DARK SOULS™ III")
            .performClick()

        // Verify App Page exists
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("AppPageList")
        )
    }
}
