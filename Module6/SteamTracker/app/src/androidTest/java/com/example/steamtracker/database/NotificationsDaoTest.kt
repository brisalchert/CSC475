package com.example.steamtracker.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.example.steamtracker.TestDatabase
import com.example.steamtracker.fake.FakeNewsNotification
import com.example.steamtracker.fake.FakeWishlistNotification
import com.example.steamtracker.room.dao.NotificationsDao
import com.example.steamtracker.room.entities.NewsNotificationEntity
import com.example.steamtracker.room.entities.WishlistNotificationEntity
import com.example.steamtracker.room.relations.NewsNotificationWithDetails
import com.example.steamtracker.room.relations.WishlistNotificationWithDetails
import com.example.steamtracker.utils.toAppDetailsNotificationEntity
import com.example.steamtracker.utils.toNewsItemNotificationEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class NotificationsDaoTest {
    private lateinit var notificationsDao: NotificationsDao
    private lateinit var db: TestDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            TestDatabase::class.java
        ).build()
        notificationsDao = db.notificationsDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeNewsNotificationAndReadInList() =
        runTest {
            val fakeNewsNotificationEntity = NewsNotificationEntity(0L)
            val fakeNewsItemNotificationEntities = FakeNewsNotification.response.newPosts.map {
                it.toNewsItemNotificationEntity(0L)
            }
            val fakeNewsNotificationWithDetails = NewsNotificationWithDetails(
                notification = fakeNewsNotificationEntity,
                newPosts = fakeNewsItemNotificationEntities
            )

            notificationsDao.insertNewsNotificationWithPosts(
                notification = fakeNewsNotificationEntity,
                newPosts = fakeNewsItemNotificationEntities
            )

            var allNewsNotifications = notificationsDao.getAllNewsNotifications().first()

            // Test NewsNotification fetched
            assertThat(allNewsNotifications[0], equalTo(fakeNewsNotificationWithDetails))

            notificationsDao.deleteNewsNotification(0L)

            allNewsNotifications = notificationsDao.getAllNewsNotifications().first()

            // Test NewsNotification removed
            assertThat(allNewsNotifications, equalTo(emptyList()))
        }

    @Test
    @Throws(Exception::class)
    fun writeWishlistNotificationAndReadInList() =
        runTest {
            val fakeWishlistNotificationEntity = WishlistNotificationEntity(0L)
            val fakeWishlistItemNotificationEntities = FakeWishlistNotification.response.newSales.map {
                it.toAppDetailsNotificationEntity(0L)
            }
            val fakeWishlistNotificationWithDetails = WishlistNotificationWithDetails(
                notification = fakeWishlistNotificationEntity,
                newSales = fakeWishlistItemNotificationEntities
            )

            notificationsDao.insertWishlistNotificationWithAppDetails(
                notification = fakeWishlistNotificationEntity,
                newSales = fakeWishlistItemNotificationEntities
            )

            var allWishlistNotifications = notificationsDao.getAllWishlistNotifications().first()

            // Test NewsNotification fetched
            assertThat(allWishlistNotifications[0], equalTo(fakeWishlistNotificationWithDetails))

            notificationsDao.deleteWishlistNotification(0L)

            allWishlistNotifications = notificationsDao.getAllWishlistNotifications().first()

            // Test NewsNotification removed
            assertThat(allWishlistNotifications, equalTo(emptyList()))
        }
}
