package com.example.steamtracker.utils

import com.example.steamtracker.model.CollectionApp
import com.example.steamtracker.room.relations.CollectionWithApps
import kotlin.collections.forEach

fun mapCollectionEntitiesToCollections(entities: List<CollectionWithApps>): Map<String, List<CollectionApp>> {
    val collections = mutableMapOf<String, List<CollectionApp>>()

    entities.forEach { entity ->
        val collectionApps = entity.collectionAppsDetails.map {
            CollectionApp(it.collectionName, it.appid, it.index)
        }

        collections.put(entity.collection.name, collectionApps)
    }

    return collections
}
