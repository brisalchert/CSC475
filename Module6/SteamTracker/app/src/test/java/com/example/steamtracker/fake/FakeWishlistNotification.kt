package com.example.steamtracker.fake

import com.example.steamtracker.model.WishlistNotification

object FakeWishlistNotification {
    val response: WishlistNotification = WishlistNotification(
        newSales = listOf(),
        timestamp = 0L
    )
}
