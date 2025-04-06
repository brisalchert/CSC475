package com.example.steamtracker.ui

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.pressKey
import androidx.test.core.app.ApplicationProvider
import com.example.steamtracker.SteamTrackerApplication
import com.example.steamtracker.TestAppContainer
import com.example.steamtracker.setupMockWebServer
import com.example.steamtracker.ui.theme.SteamTrackerTheme
import okhttp3.mockwebserver.MockWebServer
import okhttp3.tls.internal.TlsUtil
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

class MenuScreenTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockWebServer: MockWebServer

    @OptIn(ExperimentalTestApi::class)
    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<SteamTrackerApplication>()

        // Generate an SSL certificate and private key using OkHttp-TLS
        val certificateAndKey = TlsUtil.localhost()

        // Get the SSLSocketFactory and X509TrustManager from the generated certificate and key
        val sslSocketFactory: SSLSocketFactory = certificateAndKey.sslSocketFactory()
        val trustManager: X509TrustManager = certificateAndKey.trustManager

        // Initialize MockWebServer for HTTPS requests
        mockWebServer = MockWebServer()
        setupMockWebServer(mockWebServer, sslSocketFactory)
        mockWebServer.start()

        // Inject TestAppContainer into the application
        context.container = TestAppContainer(context, mockWebServer, sslSocketFactory, trustManager)

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

    @After
    fun teardown() {
        mockWebServer.shutdown()
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

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun menuPreferences_verifyPreferencesAddedRemoved() {
        // Switch to Search screen
        composeTestRule.onNodeWithTag("MenuList")
            .onChildAt(4)
            .assertTextEquals("Search")
            .performClick()
        composeTestRule.waitUntilExactlyOneExists(
            hasText("Search the Steam Store")
        )

        // Search for game
        composeTestRule.onNodeWithText("Search the Steam Store")
            .performClick()
            .performTextInput("Elden Ring")
        composeTestRule.onNodeWithText("Elden Ring")
            .performKeyInput {
                pressKey(Key.Enter)
            }

        // Wait for search results
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("SearchResultsList")
        )

        // Select game
        composeTestRule.onNodeWithText("ELDEN RING")
            .performClick()

        // Wait for app screen
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("AppPageList")
        )

        // Add a genre and a tag
        composeTestRule.onNodeWithTag("AppPageList")
            .performScrollToNode(
                hasTestTag("TagsList")
            )
        composeTestRule.onNodeWithTag("GenresList")
            .onChildAt(0)
            .performClick()
        composeTestRule.onNodeWithTag("TagsList")
            .onChildAt(0)
            .performClick()

        // Navigate to Preferences screen
        composeTestRule.onNodeWithContentDescription("Menu")
            .performClick()
        composeTestRule.onNodeWithTag("MenuList")
            .onChildAt(5)
            .assertTextEquals("Preferences")
            .performClick()

        // Verify genre and tag present
        composeTestRule.onNodeWithContentDescription("Remove genre")
            .assertExists()
        composeTestRule.onNodeWithContentDescription("Remove tag")
            .assertExists()

        // Remove genre and tag
        composeTestRule.onNodeWithContentDescription("Remove genre")
            .performClick()
        composeTestRule.onNodeWithContentDescription("Remove tag")
            .performClick()

        // Verify genre and tag removed
        composeTestRule.waitUntilExactlyOneExists(
            hasText("Add genres to see them here!")
        )
        composeTestRule.waitUntilExactlyOneExists(
            hasText("Add tags to see them here!")
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun menuSettings_verifyRecommendationsToggled() {
        // Switch to Settings screen
        composeTestRule.onNodeWithTag("MenuList")
            .onChildAt(6)
            .assertTextEquals("Settings")
            .performClick()

        // Toggle recommendations off
        composeTestRule.onNodeWithTag("RecommendationsSwitch")
            .performClick()

        // Navigate to recommended tab
        composeTestRule.onNodeWithContentDescription("Store")
            .performClick()
        composeTestRule.onNodeWithText("Recommended")
            .performClick()

        // Verify "For You" header does not exist
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("RecommendationsList")
        )
        composeTestRule.onNodeWithText("FOR YOU")
            .assertDoesNotExist()

        // Navigate back to Settings screen
        composeTestRule.onNodeWithContentDescription("Menu")
            .performClick()
        composeTestRule.onNodeWithTag("MenuList")
            .onChildAt(6)
            .assertTextEquals("Settings")
            .performClick()

        // Toggle recommendations back on
        composeTestRule.onNodeWithTag("RecommendationsSwitch")
            .performClick()

        // Navigate back to recommended tab
        composeTestRule.onNodeWithContentDescription("Store")
            .performClick()
        composeTestRule.onNodeWithText("Recommended")
            .performClick()

        // Verify "For You" header does exist
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("RecommendationsList")
        )
        composeTestRule.onNodeWithText("FOR YOU")
            .assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun menuSettings_verifyTopSellersToggled() {
        // Switch to Settings screen
        composeTestRule.onNodeWithTag("MenuList")
            .onChildAt(6)
            .assertTextEquals("Settings")
            .performClick()

        // Toggle top sellers off
        composeTestRule.onNodeWithTag("TopSellersSwitch")
            .performClick()

        // Navigate to featured tab
        composeTestRule.onNodeWithContentDescription("Store")
            .performClick()

        // Verify "Top Sellers" header does not exist
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("FeaturedGamesList")
        )
        composeTestRule.onAllNodes(hasText("TOP SELLERS"))
            .assertCountEquals(0)

        // Navigate back to Settings screen
        composeTestRule.onNodeWithContentDescription("Menu")
            .performClick()
        composeTestRule.onNodeWithTag("MenuList")
            .onChildAt(6)
            .assertTextEquals("Settings")
            .performClick()

        // Toggle top sellers back on
        composeTestRule.onNodeWithTag("TopSellersSwitch")
            .performClick()

        // Navigate back to featured tab
        composeTestRule.onNodeWithContentDescription("Store")
            .performClick()

        // Verify "Top Sellers" header does exist
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("FeaturedGamesList")
        )
        composeTestRule.onNodeWithTag("FeaturedGamesList")
            .performScrollToNode(hasText("TOP SELLERS"))
            .assertExists()
    }
}
