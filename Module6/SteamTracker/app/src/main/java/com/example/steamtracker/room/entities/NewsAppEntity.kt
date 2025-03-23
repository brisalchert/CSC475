package com.example.steamtracker.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_apps")
data class NewsAppEntity(
    @PrimaryKey val appid: Int
)
