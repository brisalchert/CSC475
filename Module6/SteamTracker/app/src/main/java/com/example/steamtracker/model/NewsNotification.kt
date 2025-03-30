package com.example.steamtracker.model

data class NewsNotification(
    val newPosts: List<NewsItem> = emptyList(),
    val timestamp: Long
)
