package com.example.steamtracker.ui

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
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

class MenuScreenTests {
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

        // Switch to menu screen
        composeTestRule.onNodeWithContentDescription("Menu")
            .performClick()

        // Wait for menu to show
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("MenuList")
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun menu_verifyStoreAlternativeNavigation() {
        // Switch to Store screen
        composeTestRule.onNodeWithTag("MenuList")
            .onChildAt(0)
            .assertTextEquals("Store")
            .performClick()
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("FeaturedGamesList")
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun menu_verifyNewsAlternativeNavigation() {
        // Switch to News screen
        composeTestRule.onNodeWithTag("MenuList")
            .onChildAt(1)
            .assertTextEquals("News")
            .performClick()
        composeTestRule.waitUntilExactlyOneExists(
            hasText("Add apps to your news list to see posts here!")
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun menu_verifyCollectionsAlternativeNavigation() {
        // Switch to Collections screen
        composeTestRule.onNodeWithTag("MenuList")
            .onChildAt(2)
            .assertTextEquals("Collections")
            .performClick()
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("CollectionsList")
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun menu_verifyNotificationsAlternativeNavigation() {
        // Switch to Notifications screen
        composeTestRule.onNodeWithTag("MenuList")
            .onChildAt(3)
            .assertTextEquals("Notifications")
            .performClick()
        composeTestRule.waitUntilExactlyOneExists(
            hasText("No notifications to view!")
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun menu_verifySearchAlternativeNavigation() {
        // Switch to Search screen
        composeTestRule.onNodeWithTag("MenuList")
            .onChildAt(4)
            .assertTextEquals("Search")
            .performClick()
        composeTestRule.waitUntilExactlyOneExists(
            hasText("Search the Steam Store")
        )
    }
}
