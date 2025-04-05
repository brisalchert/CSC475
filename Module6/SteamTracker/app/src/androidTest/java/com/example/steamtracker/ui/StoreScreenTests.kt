package com.example.steamtracker.ui

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasContentDescription
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
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.pressKey
import com.example.steamtracker.ui.theme.SteamTrackerTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StoreScreenTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        // Set up the User interface for tests
        composeTestRule.setContent {
            SteamTrackerTheme {
                SteamTrackerApp()
            }
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun featuredGame_verifyAppPageShows() {
        // Wait for the Featured tab to load
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("FeaturedGamesList")
        )

        composeTestRule.onNodeWithTag("FeaturedGamesList")
            .performScrollToIndex(3).performClick()

        // Verify App Page text exists
        composeTestRule.waitUntilExactlyOneExists(
            hasText("SCREENSHOTS")
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun salesGame_verifyAppPageShows() {
        composeTestRule.waitUntilExactlyOneExists(
            hasText("On Sale")
        )

        composeTestRule.onNodeWithText("On Sale").performClick()

        // Wait for the Sales tab to load
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("SalesGamesList")
        )

        composeTestRule.onNodeWithTag("SalesGamesList")
            .performScrollToIndex(5)
            .performClick()

        // Verify App Page text exists
        composeTestRule.waitUntilExactlyOneExists(
            hasText("SCREENSHOTS")
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun recommendedGame_verifyAppPageShows() {
        composeTestRule.waitUntilExactlyOneExists(
            hasText("Recommended")
        )

        composeTestRule.onNodeWithText("Recommended").performClick()

        // Wait for the Recommended tab to load
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("RecommendationsList")
        )

        composeTestRule.onNodeWithTag("RecommendationsList")
            .performScrollToIndex(5)
            .performClick()

        // Verify App Page text exists
        composeTestRule.waitUntilExactlyOneExists(
            hasText("SCREENSHOTS")
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun wishlistBox_verifyWishlistGameAddedRemoved() {
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

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun favoritesBox_verifyFavoriteAddedRemoved() {
        // Wait for the Recommended tab to load
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("FeaturedGamesList")
        )

        composeTestRule.onNodeWithTag("FeaturedGamesList")
            .performScrollToIndex(1)
            .performClick()

        // Verify App Page text exists
        composeTestRule.waitUntilExactlyOneExists(
            hasText("Add to Favorites")
        )

        // Add game to favorites
        composeTestRule.onNodeWithText("Add to Favorites")
            .performClick()

        // Verify Added status
        composeTestRule.onNodeWithContentDescription("Added to Favorites")
            .assertExists()

        // Remove game from favorites
        composeTestRule.onNodeWithText("Add to Favorites")
            .performClick()

        // Verify Removed status
        composeTestRule.onNodeWithContentDescription("Not on Favorites")
            .assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun newsListBox_verifyTrackedAppAddedRemoved() {
        // Wait for the Recommended tab to load
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("FeaturedGamesList")
        )

        composeTestRule.onNodeWithTag("FeaturedGamesList")
            .performScrollToIndex(1)
            .performClick()

        // Verify App Page text exists
        composeTestRule.waitUntilExactlyOneExists(
            hasText("Track News")
        )

        // Add game to wishlist
        composeTestRule.onNodeWithText("Track News")
            .performClick()

        // Verify Added status
        composeTestRule.onNodeWithContentDescription("Added to News list")
            .assertExists()

        // Remove game from wishlist
        composeTestRule.onNodeWithText("Track News")
            .performClick()

        // Verify Removed status
        composeTestRule.onNodeWithContentDescription("Not on News list")
            .assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun screenshot_verifyFullscreenScreenshotAndBackButton() {
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

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun genre_verifyGenreAddedRemoved() {
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

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun tag_verifyTagAddedRemoved() {
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

        // Scroll to Tags
        composeTestRule.onNodeWithTag("AppPageList")
            .performScrollToNode(
                hasTestTag("TagsList")
            )

        // Add tag to favorites
        composeTestRule.onNodeWithTag("TagsList")
            .onChildAt(0)
            .performClick()

        // Verify Added status and remove tag from favorites
        composeTestRule.onNodeWithTag("TagsList")
            .onChildAt(0)
            .assertContentDescriptionEquals("Tag Added to Favorites")
            .performClick()

        // Verify Removed status
        composeTestRule.onNodeWithTag("TagsList")
            .onChildAt(0)
            .assertContentDescriptionEquals("Tag not on Favorites")
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun autocompleteResults_verifyAccessAppPage() {
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

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun searchResults_verifyResults() {
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

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun sortSearchResults_verifyResultsSorted() {
        // Search for Elden Ring
        composeTestRule.onNodeWithText("Search the Steam Store")
            .performClick()
            .performTextInput("Elden Ring")
        composeTestRule.onNodeWithText("Elden Ring")
            .performKeyInput {
                pressKey(Key.Enter)
            }
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("SearchResultsList")
        )

        // Sort by minimum price
        composeTestRule.onNodeWithContentDescription("Sort Results")
            .performClick()
        composeTestRule.onNodeWithText("Sort by Minimum Price")
            .performClick()

        // Verify minimum price app on top
        composeTestRule.onNodeWithTag("SearchResultsList")
            .onChildAt(0)
            .assertTextContains("ELDEN RING NIGHTREIGN")

        // Sort reverse alphabetically
        composeTestRule.onNodeWithContentDescription("Sort Results")
            .performClick()
        composeTestRule.onNodeWithText("Sort Reverse Alphabetical")
            .performClick()

        // Verify last alphabetical on top
        composeTestRule.onNodeWithTag("SearchResultsList")
            .onChildAt(0)
            .assertTextContains("ELDEN RING Shadow of the Erdtree Premium Bundle")
    }
}
