package com.example.steamtracker.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_id_aliases")
data class AppIdAliasEntity(
    @PrimaryKey val aliasId: Int,
    val canonicalId: Int
)
