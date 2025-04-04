package com.example.steamtracker.fake

import com.example.steamtracker.model.NewsNotification

object FakeNewsNotification {
    val response: NewsNotification = NewsNotification(
        newPosts = listOf(),
        timestamp = 0L
    )
}
