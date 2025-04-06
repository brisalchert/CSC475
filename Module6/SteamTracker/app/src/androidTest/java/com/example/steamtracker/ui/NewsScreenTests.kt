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

class NewsScreenTests {
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

    @After
    fun teardown() {
        mockWebServer.shutdown()
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
