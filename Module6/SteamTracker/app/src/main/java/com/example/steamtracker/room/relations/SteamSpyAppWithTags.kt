package com.example.steamtracker.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.steamtracker.room.entities.SteamSpyAppEntity
import com.example.steamtracker.room.entities.TagEntity

data class SteamSpyAppWithTags(
    @Embedded val app: SteamSpyAppEntity,
    @Relation(
        parentColumn = "appid",
        entityColumn = "appid"
    )
    val tags: List<TagEntity>
)
