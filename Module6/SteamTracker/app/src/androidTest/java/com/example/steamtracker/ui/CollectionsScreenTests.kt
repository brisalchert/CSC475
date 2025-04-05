package com.example.steamtracker.ui

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.pressKey
import com.example.steamtracker.ui.theme.SteamTrackerTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CollectionsScreenTests {
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

        // Switch to collections screen
        composeTestRule.onNodeWithContentDescription("Collections")
            .performClick()

        // Wait for collections to show
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("CollectionsList")
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun wishlist_verifyWishlistGameAddedRemoved() {
        // Select Wishlist
        composeTestRule.onNodeWithText("Wishlist")
            .performClick()

        // Select Add App
        composeTestRule.onNodeWithContentDescription("Add App")
            .performClick()

        // Search for app to add
        composeTestRule.onNodeWithText("Search the Steam Store")
            .performClick()
            .performTextInput("Elden")
        composeTestRule.onNodeWithText("Elden")
            .performKeyInput {
                pressKey(Key.Enter)
            }

        // Wait for search results
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("CollectionsSearchList")
        )

        // Add app to wishlist
        composeTestRule.onNodeWithTag("CollectionsSearchList")
            .onChildAt(0)
            .onChild()
            .assertContentDescriptionEquals("Add to Collection")
            .performClick()

        // Go back to wishlist
        composeTestRule.onNodeWithContentDescription("Back Button")
            .performClick()

        // Verify app in wishlist
        composeTestRule.waitUntilExactlyOneExists(
            hasText("ELDEN RING")
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun favorites_verifyFavoriteGameAddedRemoved() {
        // Select Favorites
        composeTestRule.onNodeWithText("Favorites")
            .performClick()

        // Select Add App
        composeTestRule.onNodeWithContentDescription("Add App")
            .performClick()

        // Search for app to add
        composeTestRule.onNodeWithText("Search the Steam Store")
            .performClick()
            .performTextInput("Elden")
        composeTestRule.onNodeWithText("Elden")
            .performKeyInput {
                pressKey(Key.Enter)
            }

        // Wait for search results
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("CollectionsSearchList")
        )

        // Add app to favorites
        composeTestRule.onNodeWithTag("CollectionsSearchList")
            .onChildAt(0)
            .onChild()
            .assertContentDescriptionEquals("Add to Collection")
            .performClick()

        // Go back to favorites
        composeTestRule.onNodeWithContentDescription("Back Button")
            .performClick()

        // Verify app in favorites
        composeTestRule.waitUntilExactlyOneExists(
            hasText("ELDEN RING")
        )
    }
}
