package com.example.steamtracker.utils

import com.example.steamtracker.model.FeaturedCategoriesRequest
import com.example.steamtracker.model.RegularCategory
import com.example.steamtracker.model.SpotlightCategory
import com.example.steamtracker.model.SpotlightItem
import com.example.steamtracker.model.StaticCategory
import com.example.steamtracker.room.entities.AppInfoEntity
import com.example.steamtracker.room.entities.FeaturedCategoryEntity
import com.example.steamtracker.room.entities.SpotlightItemEntity
import com.example.steamtracker.room.relations.FeaturedCategoryWithDetails
import java.util.Collections.addAll
import kotlin.collections.component1
import kotlin.collections.component2

fun FeaturedCategoriesRequest.mapToFeaturedCategoryEntities(): List<FeaturedCategoryEntity> {
    val entities = mutableListOf<FeaturedCategoryEntity>()

    this.specials?.let {
        entities.add(FeaturedCategoryEntity(it.id, it.name, "regular", this.status, System.currentTimeMillis()))
    }
    this.comingSoon?.let {
        entities.add(FeaturedCategoryEntity(it.id, it.name, "regular", this.status, System.currentTimeMillis()))
    }
    this.topSellers?.let {
        entities.add(FeaturedCategoryEntity(it.id, it.name, "regular", this.status, System.currentTimeMillis()))
    }
    this.newReleases?.let {
        entities.add(FeaturedCategoryEntity(it.id, it.name, "regular", this.status, System.currentTimeMillis()))
    }
    this.genres?.let {
        entities.add(FeaturedCategoryEntity(it.id, it.name, "static", this.status, System.currentTimeMillis()))
    }
    this.trailerslideshow?.let {
        entities.add(FeaturedCategoryEntity(it.id, it.name, "static", this.status, System.currentTimeMillis()))
    }

    this.spotlightCategories?.forEach { (_, value) ->
        if (value is SpotlightCategory) {
            entities.add(FeaturedCategoryEntity(value.id, value.name, "spotlight", this.status, System.currentTimeMillis()))
        } else if (value is RegularCategory) {
            entities.add(FeaturedCategoryEntity(value.id, value.name, "regular", this.status, System.currentTimeMillis()))
        }
    }

    return entities
}

fun FeaturedCategoriesRequest.mapToAppInfoEntities(): List<AppInfoEntity> {
    val appInfoEntities = mutableListOf<AppInfoEntity>()

    this.specials?.let {
        appInfoEntities.addAll(it.items?.toAppInfoEntityList(it.id) ?: emptyList())
    }
    this.comingSoon?.let {
        appInfoEntities.addAll(it.items?.toAppInfoEntityList(it.id) ?: emptyList())
    }
    this.topSellers?.let {
        appInfoEntities.addAll(it.items?.toAppInfoEntityList(it.id) ?: emptyList())
    }
    this.newReleases?.let {
        appInfoEntities.addAll(it.items?.toAppInfoEntityList(it.id) ?: emptyList())
    }

    return appInfoEntities
}

fun FeaturedCategoriesRequest.mapToSpotlightItemEntities(): List<SpotlightItemEntity> {
    val spotlightEntities = mutableListOf<SpotlightItemEntity>()

    this.spotlightCategories?.forEach { (_, value) ->
        if (value is SpotlightCategory) {
            value.items?.forEach { item ->
                spotlightEntities.add(
                    SpotlightItemEntity(
                        name = item.name,
                        categoryId = value.id,
                        headerImage = item.headerImage,
                        body = item.body,
                        url = item.url
                    )
                )
            }
        }
    }

    return spotlightEntities
}

fun mapToFeaturedCategoriesRequest(entities: List<FeaturedCategoryWithDetails>): FeaturedCategoriesRequest {
    val spotlightCategories = mutableMapOf<String, Any>()

    val specials = entities.find { it.category.type == "regular" && it.category.name == "Specials" }?.let {
        RegularCategory(it.category.id, it.category.name, it.appItems?.map { it.toAppInfo() })
    }

    val comingSoon = entities.find { it.category.type == "regular" && it.category.name == "Coming Soon" }?.let {
        RegularCategory(it.category.id, it.category.name, it.appItems?.map { it.toAppInfo() })
    }

    val topSellers = entities.find { it.category.type == "regular" && it.category.name == "Top Sellers" }?.let {
        RegularCategory(it.category.id, it.category.name, it.appItems?.map { it.toAppInfo() })
    }

    val newReleases = entities.find { it.category.type == "regular" && it.category.name == "New Releases" }?.let {
        RegularCategory(it.category.id, it.category.name, it.appItems?.map { it.toAppInfo() })
    }

    val genres = entities.find { it.category.type == "static" && it.category.name == "Genres" }?.let {
        StaticCategory(it.category.id, it.category.name)
    }

    val trailerslideshow = entities.find { it.category.type == "static" && it.category.name == "Trailer Slideshow" }?.let {
        StaticCategory(it.category.id, it.category.name)
    }

    entities.filter { it.category.type == "spotlight" }.forEach {
        spotlightCategories[it.category.id] = SpotlightCategory(
            it.category.id, it.category.name, it.spotlightItems?.map { item ->
                SpotlightItem(item.name, item.headerImage, item.body, item.url)
            }
        )
    }

    return FeaturedCategoriesRequest(
        spotlightCategories = spotlightCategories,
        specials = specials,
        comingSoon = comingSoon,
        topSellers = topSellers,
        newReleases = newReleases,
        genres = genres,
        trailerslideshow = trailerslideshow,
        status = 1
    )
}
