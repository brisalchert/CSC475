package com.example.steamtracker.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "app_news_requests")
data class AppNewsRequestEntity(
    @PrimaryKey val appid: Int,
    val lastUpdated: Long
)

@Entity(
    primaryKeys = ["appid"],
    foreignKeys = [
        ForeignKey(
            entity = AppNewsRequestEntity::class,
            parentColumns = ["appid"],
            childColumns = ["appid"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AppNewsEntity(
    val appid: Int
)

@Entity(
    tableName = "news_items",
    primaryKeys = ["appid", "gid"],
    foreignKeys = [
        ForeignKey(
            entity = AppNewsEntity::class,
            parentColumns = ["appid"],
            childColumns = ["appid"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class NewsItemEntity(
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
    val appid: Int
)
