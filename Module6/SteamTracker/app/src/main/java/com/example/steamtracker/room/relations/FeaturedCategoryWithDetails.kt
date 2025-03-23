package com.example.steamtracker.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.steamtracker.room.entities.FeaturedCategoryEntity
import com.example.steamtracker.room.entities.AppInfoEntity
import com.example.steamtracker.room.entities.SpotlightItemEntity

data class FeaturedCategoryWithDetails(
    @Embedded val category: FeaturedCategoryEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val appItems: List<AppInfoEntity>?,

    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val spotlightItems: List<SpotlightItemEntity>?
)
