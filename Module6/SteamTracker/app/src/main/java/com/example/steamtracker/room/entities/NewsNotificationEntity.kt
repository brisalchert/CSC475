package com.example.steamtracker.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "news_notifications")
data class NewsNotificationEntity(
    @PrimaryKey val timestamp: Long
)

@Entity(
    tableName = "news_items_notifications",
    primaryKeys = ["timestamp", "gid"],
    foreignKeys = [
        ForeignKey(
            entity = NewsNotificationEntity::class,
            parentColumns = ["timestamp"],
            childColumns = ["timestamp"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class NewsItemNotificationEntity(
    val gid: String,
    val title: String,
    val url: String,
    val isExternalUrl: Boolean,
    val author: String,
    val contents: String,
    val feedlabel: String,
    val date: Long,
    val feedname: String,
    val feedType: Int,
    val appid: Int,
    val timestamp: Long
)
