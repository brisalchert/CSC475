package com.example.steamtracker

import com.example.steamtracker.data.NetworkNotificationsRepository
import com.example.steamtracker.fake.FakeNewsNotification
import com.example.steamtracker.fake.FakeWishlistNotification
import com.example.steamtracker.room.dao.NotificationsDao
import com.example.steamtracker.room.entities.NewsNotificationEntity
import com.example.steamtracker.room.entities.WishlistNotificationEntity
import com.example.steamtracker.room.relations.NewsNotificationWithDetails
import com.example.steamtracker.room.relations.WishlistNotificationWithDetails
import com.example.steamtracker.utils.toAppDetailsNotificationEntity
import com.example.steamtracker.utils.toNewsItemNotificationEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class NetworkNotificationsRepositoryTest {
    private lateinit var mockNotificationsDao: NotificationsDao
    private lateinit var repository: NetworkNotificationsRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        mockNotificationsDao = Mockito.mock(NotificationsDao::class.java)
    }

    @Test
    fun networkNotificationsRepository_getAllWishlistNotifications_verifyNotificationsCorrect() =
        runTest {
            val fakeWishlistNotificationWithDetails = WishlistNotificationWithDetails(
                notification = WishlistNotificationEntity(0L),
                newSales = FakeWishlistNotification.response.newSales.map {
                    it.toAppDetailsNotificationEntity(0L)
                }
            )
            val fakeWishlistNotificationFlow = flowOf(
                listOf(fakeWishlistNotificationWithDetails)
            )

            // Mock DAO behavior
            `when`(mockNotificationsDao.getAllWishlistNotifications()).thenReturn(
                fakeWishlistNotificationFlow
            )

            repository = NetworkNotificationsRepository(
                notificationsDao = mockNotificationsDao
            )

            assertEquals(
                listOf(fakeWishlistNotificationWithDetails),
                repository.wishlistNotifications.first()
            )
        }

    @Test
    fun networkNotificationsRepository_getAllNewsNotifications_verifyNotificationsCorrect() =
        runTest {
            val fakeNewsNotificationWithDetails = NewsNotificationWithDetails(
                notification = NewsNotificationEntity(0L),
                newPosts = FakeNewsNotification.response.newPosts.map {
                    it.toNewsItemNotificationEntity(0L)
                }
            )
            val fakeNewsNotificationFlow = flowOf(
                listOf(fakeNewsNotificationWithDetails)
            )

            // Mock DAO behavior
            `when`(mockNotificationsDao.getAllNewsNotifications()).thenReturn(
                fakeNewsNotificationFlow
            )

            repository = NetworkNotificationsRepository(
                notificationsDao = mockNotificationsDao
            )

            assertEquals(
                listOf(fakeNewsNotificationWithDetails),
                repository.newsNotifications.first()
            )
        }
}
