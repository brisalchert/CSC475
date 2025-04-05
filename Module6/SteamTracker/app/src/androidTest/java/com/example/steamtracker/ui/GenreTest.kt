package com.example.steamtracker.ui

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.pressKey
import com.example.steamtracker.ui.theme.SteamTrackerTheme
import org.junit.Rule
import org.junit.Test

class GenreTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun genre_verifyGenreAddedRemoved() {
        composeTestRule.setContent {
            SteamTrackerTheme {
                SteamTrackerApp()
            }
        }

        // Search for Elden Ring
        composeTestRule.onNodeWithText("Search the Steam Store")
            .performClick()
            .performTextInput("Elden")
        composeTestRule.onNodeWithText("Elden")
            .performKeyInput {
                pressKey(Key.Enter)
            }
        composeTestRule.waitUntilExactlyOneExists(
            hasText("ELDEN RING")
        )
        composeTestRule.onNodeWithText("ELDEN RING")
            .performClick()

        // Verify App Page exists
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("AppPageList")
        )

        // Scroll to Genres
        composeTestRule.onNodeWithTag("AppPageList")
            .performScrollToNode(
                hasTestTag("GenresList")
            )

        // Add genre to favorites
        composeTestRule.onNodeWithTag("GenresList")
            .onChildAt(0)
            .performClick()

        // Verify Added status and remove genre from favorites
        composeTestRule.onNodeWithTag("GenresList")
            .onChildAt(0)
            .assertContentDescriptionEquals("Genre Added to Favorites")
            .performClick()

        // Verify Removed status
        composeTestRule.onNodeWithTag("GenresList")
            .onChildAt(0)
            .assertContentDescriptionEquals("Genre not on Favorites")
    }
}
