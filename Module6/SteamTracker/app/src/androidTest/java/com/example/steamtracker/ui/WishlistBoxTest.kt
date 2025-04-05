package com.example.steamtracker.ui

import androidx.compose.ui.test.ExperimentalTestApi
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

class WishlistBoxTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun wishlistBox_verifyWishlistGameAddedRemoved() {
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
            hasText("Add to Wishlist")
        )

        // Add game to wishlist
        composeTestRule.onNodeWithText("Add to Wishlist")
            .performClick()

        // Verify Added status
        composeTestRule.onNodeWithContentDescription("Added to Wishlist")
            .assertExists()

        // Remove game from wishlist
        composeTestRule.onNodeWithText("Add to Wishlist")
            .performClick()

        // Verify Removed status
        composeTestRule.onNodeWithContentDescription("Not on Wishlist")
            .assertExists()
    }
}
