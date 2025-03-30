package com.example.steamtracker.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.steamtracker.room.entities.AppDetailsNotificationEntity
import com.example.steamtracker.room.entities.NewsItemNotificationEntity
import com.example.steamtracker.room.entities.NewsNotificationEntity
import com.example.steamtracker.room.entities.WishlistNotificationEntity
import com.example.steamtracker.room.relations.NewsNotificationWithDetails
import com.example.steamtracker.room.relations.WishlistNotificationWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsNotification(notification: NewsNotificationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlistNotification(notification: WishlistNotificationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsItemsNotifications(newPosts: List<NewsItemNotificationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppDetailsNotifications(newSales: List<AppDetailsNotificationEntity>)

    @Transaction
    suspend fun insertNewsNotificationWithPosts(notification: NewsNotificationEntity, newPosts: List<NewsItemNotificationEntity>) {
        insertNewsNotification(notification)
        insertNewsItemsNotifications(newPosts)
    }

    @Transaction
    suspend fun insertWishlistNotificationWithAppDetails(notification: WishlistNotificationEntity, newSales: List<AppDetailsNotificationEntity>) {
        insertWishlistNotification(notification)
        insertAppDetailsNotifications(newSales)
    }

    @Query("DELETE FROM news_notifications WHERE timestamp = :timestamp")
    suspend fun deleteNewsNotification(timestamp: Long)

    @Query("DELETE FROM wishlist_notifications WHERE timestamp = :timestamp")
    suspend fun deleteWishlistNotification(timestamp: Long)

    @Transaction
    @Query("SELECT * FROM news_notifications")
    fun getAllNewsNotifications(): Flow<List<NewsNotificationWithDetails>>

    @Transaction
    @Query("SELECT * FROM wishlist_notifications")
    fun getAllWishlistNotifications(): Flow<List<WishlistNotificationWithDetails>>
}
