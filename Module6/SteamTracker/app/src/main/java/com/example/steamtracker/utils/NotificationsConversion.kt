package com.example.steamtracker.utils

import com.example.steamtracker.model.NewsNotification
import com.example.steamtracker.model.WishlistNotification
import com.example.steamtracker.room.relations.NewsNotificationWithDetails
import com.example.steamtracker.room.relations.WishlistNotificationWithDetails

fun NewsNotificationWithDetails.mapToNewsNotification(): NewsNotification {
    return NewsNotification(
        timestamp = this.notification.timestamp,
        newPosts = this.newPosts.map { it.toNewsItem() }
    )
}

fun WishlistNotificationWithDetails.mapToWishlistNotification(): WishlistNotification {
    return WishlistNotification(
        timestamp = this.notification.timestamp,
        newSales = this.newSales.map { it.toAppDetails() }
    )
}
