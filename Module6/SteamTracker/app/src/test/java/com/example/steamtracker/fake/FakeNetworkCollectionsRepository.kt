package com.example.steamtracker.fake

import com.example.steamtracker.data.CollectionsRepository
import com.example.steamtracker.model.CollectionApp
import com.example.steamtracker.room.entities.CollectionAppEntity
import com.example.steamtracker.room.entities.CollectionEntity
import com.example.steamtracker.room.relations.CollectionWithApps
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeNetworkCollectionsRepository (
): CollectionsRepository {
    override val allCollections: Flow<List<CollectionWithApps>> =
        flowOf(
            listOf(
                CollectionWithApps(
                    collection = CollectionEntity(
                        name = FakeCollectionApp.response.collectionName
                    ),
                    collectionAppsDetails = listOf(CollectionAppEntity(
                        collectionName = FakeCollectionApp.response.collectionName,
                        appid = FakeCollectionApp.response.appId,
                        index = FakeCollectionApp.response.index
                    ))
                )
            )
        )

    override suspend fun insertCollection(name: String) {
    }

    override suspend fun removeCollection(name: String) {
    }

    override suspend fun insertCollectionApp(collectionName: String, appId: Int): Boolean {
        return true
    }

    override suspend fun removeCollectionApp(collectionName: String, appId: Int) {
    }

    override suspend fun getCollection(name: String): List<CollectionApp>? {
        return listOf(FakeCollectionApp.response)
    }
}
