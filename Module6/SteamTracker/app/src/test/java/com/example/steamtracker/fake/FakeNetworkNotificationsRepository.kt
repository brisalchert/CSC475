package com.example.steamtracker.fake

import com.example.steamtracker.data.NotificationsRepository
import com.example.steamtracker.model.NewsNotification
import com.example.steamtracker.model.WishlistNotification
import com.example.steamtracker.room.entities.NewsNotificationEntity
import com.example.steamtracker.room.entities.WishlistNotificationEntity
import com.example.steamtracker.room.relations.NewsNotificationWithDetails
import com.example.steamtracker.room.relations.WishlistNotificationWithDetails
import com.example.steamtracker.utils.toAppDetailsNotificationEntity
import com.example.steamtracker.utils.toNewsItemNotificationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeNetworkNotificationsRepository(
): NotificationsRepository {
    override val wishlistNotifications: Flow<List<WishlistNotificationWithDetails>> =
        flowOf(
            listOf(
                WishlistNotificationWithDetails(
                    notification = WishlistNotificationEntity(
                        timestamp = 0L
                    ),
                    newSales = FakeWishlistNotification.response.newSales.map {
                        it.toAppDetailsNotificationEntity(0L)
                    }
                )
            )
        )
    override val newsNotifications: Flow<List<NewsNotificationWithDetails>> =
        flowOf(
            listOf(
                NewsNotificationWithDetails(
                    notification = NewsNotificationEntity(
                        timestamp = 0L
                    ),
                    newPosts = FakeNewsNotification.response.newPosts.map {
                        it.toNewsItemNotificationEntity(0L)
                    }
                )
            )
        )

    override suspend fun insertNewsNotification(notification: NewsNotification) {
    }

    override suspend fun insertWishlistNotification(notification: WishlistNotification) {
    }

    override suspend fun deleteNewsNotification(notification: NewsNotification) {
    }

    override suspend fun deleteWishlistNotification(notification: WishlistNotification) {
    }
}
