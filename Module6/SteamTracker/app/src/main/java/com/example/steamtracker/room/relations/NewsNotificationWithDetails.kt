package com.example.steamtracker.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.steamtracker.room.entities.NewsItemNotificationEntity
import com.example.steamtracker.room.entities.NewsNotificationEntity

data class NewsNotificationWithDetails(
    @Embedded val notification: NewsNotificationEntity,
    @Relation(
        entity = NewsItemNotificationEntity::class,
        parentColumn = "timestamp",
        entityColumn = "timestamp"
    )
    val newPosts: List<NewsItemNotificationEntity>
)
