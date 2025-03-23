package com.example.steamtracker.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.steamtracker.room.entities.AppNewsEntity
import com.example.steamtracker.room.entities.AppNewsRequestEntity
import com.example.steamtracker.room.entities.NewsItemEntity

data class AppNewsWithDetails(
    @Embedded val request: AppNewsRequestEntity,
    @Relation(
        entity = AppNewsEntity::class,
        parentColumn = "appid",
        entityColumn = "appid"
    )
    val appNewsWithItems: AppNewsWithItems
)

data class AppNewsWithItems(
    @Embedded val appnews: AppNewsEntity,
    @Relation(
        parentColumn = "appid",
        entityColumn = "appid",
    )
    val newsitems: List<NewsItemEntity>
)
