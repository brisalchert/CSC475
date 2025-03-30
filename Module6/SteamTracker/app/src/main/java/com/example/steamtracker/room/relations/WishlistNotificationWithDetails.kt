package com.example.steamtracker.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.steamtracker.room.entities.AppDetailsNotificationEntity
import com.example.steamtracker.room.entities.WishlistNotificationEntity

data class WishlistNotificationWithDetails(
    @Embedded val notification: WishlistNotificationEntity,
    @Relation(
        entity = AppDetailsNotificationEntity::class,
        parentColumn = "timestamp",
        entityColumn = "timestamp"
    )
    val newSales: List<AppDetailsNotificationEntity>
)
