package com.example.steamtracker.data

import com.example.steamtracker.model.NewsNotification
import com.example.steamtracker.model.WishlistNotification
import com.example.steamtracker.room.dao.NotificationsDao
import com.example.steamtracker.room.entities.NewsNotificationEntity
import com.example.steamtracker.room.entities.WishlistNotificationEntity
import com.example.steamtracker.room.relations.NewsNotificationWithDetails
import com.example.steamtracker.room.relations.WishlistNotificationWithDetails
import com.example.steamtracker.utils.toAppDetailsNotificationEntity
import com.example.steamtracker.utils.toNewsItemNotificationEntity
import kotlinx.coroutines.flow.Flow

interface NotificationsRepository {
    val wishlistNotifications: Flow<List<WishlistNotificationWithDetails>>
    val newsNotifications: Flow<List<NewsNotificationWithDetails>>

    suspend fun insertNewsNotification(notification: NewsNotification)
    suspend fun insertWishlistNotification(notification: WishlistNotification)
    suspend fun deleteNewsNotification(notification: NewsNotification)
    suspend fun deleteWishlistNotification(notification: WishlistNotification)
}

class NetworkNotificationsRepository(
    private val notificationsDao: NotificationsDao
): NotificationsRepository {
    override val wishlistNotifications: Flow<List<WishlistNotificationWithDetails>> =
        notificationsDao.getAllWishlistNotifications()
    override val newsNotifications: Flow<List<NewsNotificationWithDetails>> =
        notificationsDao.getAllNewsNotifications()

    override suspend fun insertNewsNotification(notification: NewsNotification) {
        notificationsDao.insertNewsNotificationWithPosts(
            NewsNotificationEntity(notification.timestamp),
            notification.newPosts.map { it.toNewsItemNotificationEntity(notification.timestamp) }
        )
    }

    override suspend fun insertWishlistNotification(notification: WishlistNotification) {
        notificationsDao.insertWishlistNotificationWithAppDetails(
            WishlistNotificationEntity(notification.timestamp),
            notification.newSales.map { it.toAppDetailsNotificationEntity(notification.timestamp) }
        )
    }

    override suspend fun deleteNewsNotification(notification: NewsNotification) {
        notificationsDao.deleteNewsNotification(notification.timestamp)
    }

    override suspend fun deleteWishlistNotification(notification: WishlistNotification) {
        notificationsDao.deleteWishlistNotification(notification.timestamp)
    }
}
