package com.example.steamtracker.ui.preview

import com.example.steamtracker.data.CollectionsRepository
import com.example.steamtracker.model.CollectionApp
import com.example.steamtracker.room.relations.CollectionWithApps
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeCollectionsRepository(): CollectionsRepository {
    override val allCollections: Flow<List<CollectionWithApps>> =
        flowOf(listOf())

    override suspend fun insertCollection(name: String) {
    }

    override suspend fun removeCollection(name: String) {
    }

    override suspend fun insertCollectionApp(collectionName: String, appId: Int): Boolean {
        return false
    }

    override suspend fun removeCollectionApp(collectionName: String, appId: Int) {
    }

    override suspend fun getCollection(name: String): List<CollectionApp>? {
        return null
    }
}
