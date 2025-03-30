package com.example.steamtracker.model

data class WishlistNotification(
    val newSales: List<AppDetails> = emptyList(),
    val timestamp: Long
)
