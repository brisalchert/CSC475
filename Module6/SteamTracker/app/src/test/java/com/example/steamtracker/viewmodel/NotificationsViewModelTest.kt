package com.example.steamtracker.viewmodel

import com.example.steamtracker.fake.FakeNetworkNotificationsRepository
import com.example.steamtracker.fake.FakeNewsNotification
import com.example.steamtracker.fake.FakeWishlistNotification
import com.example.steamtracker.rules.TestDispatcherRule
import com.example.steamtracker.ui.screens.NotificationsUiState
import com.example.steamtracker.ui.screens.NotificationsViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NotificationsViewModelTest {
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private lateinit var notificationsViewModel: NotificationsViewModel

    @Before
    fun setup() {
        notificationsViewModel = NotificationsViewModel(
            notificationsRepository = FakeNetworkNotificationsRepository()
        )
    }

    @Test
    fun notificationsViewModel_getAllNotifications_verifyNotificationsUiStateSuccess() =
        runTest {
            // View model calls getAllNotifications() on init
            assertEquals(
                NotificationsUiState.Success(
                    newsNotifications = listOf(FakeNewsNotification.response),
                    wishlistNotifications = listOf(FakeWishlistNotification.response)
                ),
                notificationsViewModel.notificationsUiState.first()
            )
        }
}
