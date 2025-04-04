package com.example.steamtracker.utils

import com.example.steamtracker.model.CollectionApp
import com.example.steamtracker.room.entities.CollectionAppEntity
import com.example.steamtracker.room.entities.CollectionEntity
import com.example.steamtracker.room.relations.CollectionWithApps
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class CollectionsConversionTest {
    private lateinit var collections: Map<String, List<CollectionApp>>
    private lateinit var collectionsWithApps: List<CollectionWithApps>

    @Before
    fun setup() {
        collections = mapOf(
            "Wishlist" to listOf(CollectionApp(
                collectionName = "Wishlist",
                appId = 12,
                index = 0
            )),
            "Favorites" to listOf(CollectionApp(
                collectionName = "Favorites",
                appId = 13,
                index = 1
            ))
        )

        collectionsWithApps = listOf(
            CollectionWithApps(
                collection = CollectionEntity(name = "Wishlist"),
                collectionAppsDetails = listOf(
                    CollectionAppEntity(
                        collectionName = "Wishlist",
                        appid = 12,
                        index = 0
                    )
                )
            ),
            CollectionWithApps(
                collection = CollectionEntity(name = "Favorites"),
                collectionAppsDetails = listOf(
                    CollectionAppEntity(
                        collectionName = "Favorites",
                        appid = 13,
                        index = 1
                    )
                )
            )
        )
    }

    @Test
    fun collectionsWithApps_toCollections_verifyCollections() {
        assertEquals(
            collections,
            mapCollectionEntitiesToCollections(collectionsWithApps)
        )
    }
}
