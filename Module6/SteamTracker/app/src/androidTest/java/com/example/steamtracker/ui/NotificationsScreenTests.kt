package com.example.steamtracker.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.example.steamtracker.SteamTrackerApplication
import com.example.steamtracker.TestAppContainer
import com.example.steamtracker.model.NewsNotification
import com.example.steamtracker.model.WishlistNotification
import com.example.steamtracker.setupMockWebServer
import com.example.steamtracker.ui.theme.SteamTrackerTheme
import com.example.steamtracker.utils.toNewsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.mockwebserver.MockWebServer
import okhttp3.tls.internal.TlsUtil
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

class NotificationsScreenTests {
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

        // Insert fake notifications into the database
        runBlocking {
            context.container.notificationsRepository.insertWishlistNotification(
                WishlistNotification(
                    newSales = listOf(
                        context.container.storeRepository.getAppDetails(1245620)!!,
                        context.container.storeRepository.getAppDetails(374320)!!,
                        context.container.storeRepository.getAppDetails(2001120)!!
                    ),
                    timestamp = System.currentTimeMillis()
                )
            )

            // Add Split Fiction to news list
            context.container.steamworksRepository.addNewsApp(2001120)

            withContext(Dispatchers.IO) {
                context.container.steamworksRepository.refreshAppNews()
            }

            val newPosts = context.container.steamworksRepository
                .getAllAppNews()
                .firstNotNullOf { if (it.appNewsWithItems.newsitems.isNotEmpty()) it else null }
                .appNewsWithItems
                .newsitems
                .map { it.toNewsItem() }

            context.container.notificationsRepository.insertNewsNotification(
                NewsNotification(
                    newPosts = newPosts,
                    timestamp = System.currentTimeMillis()
                )
            )
        }

        // Switch to notifications screen
        composeTestRule.onNodeWithContentDescription("Notifications")
            .performClick()

        // Wait for collections to show
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("NotificationsList")
        )
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun notifications_verifySelectNotificationNews() {
        // Select the news notification
        composeTestRule.onNodeWithText("New posts to view")
            .performClick()

        // Wait for news to load
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("NewsNotificationDialogList")
        )

        composeTestRule.onNodeWithTag("NewsNotificationDialogList")
            .onChildAt(0)
            .performClick()

        // Verify news page appears
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("NewsDetailsList")
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun notifications_verifySelectNotificationApp() {
        // Select the wishlist notification
        composeTestRule.onNodeWithText("Games on sale")
            .performClick()

        // Wait for the apps to load
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("WishlistNotificationDialogList")
        )

        composeTestRule.onNodeWithText("ELDEN RING")
            .performClick()

        // Verify app page appears
        composeTestRule.waitUntilExactlyOneExists(
            hasTestTag("AppPageList")
        )
    }
}
