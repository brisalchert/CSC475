package com.example.steamtracker.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.steamtracker.room.entities.CollectionAppEntity
import com.example.steamtracker.room.entities.CollectionEntity

data class CollectionWithApps(
    @Embedded val collection: CollectionEntity,
    @Relation(
        entity = CollectionAppEntity::class,
        parentColumn = "name",
        entityColumn = "collectionName"
    )
    val collectionAppsDetails: List<CollectionAppEntity> = emptyList()
)
