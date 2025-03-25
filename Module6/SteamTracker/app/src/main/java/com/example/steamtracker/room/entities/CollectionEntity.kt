package com.example.steamtracker.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "collections")
data class CollectionEntity(
    @PrimaryKey val name: String
)

@Entity(
    tableName = "collection_apps",
    primaryKeys = ["collectionName", "appid"],
    foreignKeys = [
        ForeignKey(
            entity = CollectionEntity::class,
            parentColumns = ["name"],
            childColumns = ["collectionName"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CollectionAppEntity(
    val collectionName: String,
    val appid: Int,
    val index: Int
)
