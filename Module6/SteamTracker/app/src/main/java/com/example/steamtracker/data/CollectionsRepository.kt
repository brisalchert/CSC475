package com.example.steamtracker.data

import com.example.steamtracker.model.CollectionApp
import com.example.steamtracker.room.dao.CollectionsDao
import com.example.steamtracker.room.entities.CollectionAppEntity
import com.example.steamtracker.room.entities.CollectionEntity
import com.example.steamtracker.room.relations.CollectionWithApps
import kotlinx.coroutines.flow.Flow

interface CollectionsRepository {
    val allCollections: Flow<List<CollectionWithApps>>

    suspend fun insertCollection(name: String)
    suspend fun removeCollection(name: String)
    suspend fun insertCollectionApp(collectionName: String, appId: Int): Boolean
    suspend fun removeCollectionApp(collectionName: String, appId: Int)
    suspend fun getCollection(name: String): List<CollectionApp>?
}

class NetworkCollectionsRepository(
    private val collectionsDao: CollectionsDao
): CollectionsRepository {
    override val allCollections: Flow<List<CollectionWithApps>> =
        collectionsDao.getAllCollections()

    override suspend fun insertCollection(name: String) {
        collectionsDao.insertCollection(CollectionEntity(name))
    }

    override suspend fun removeCollection(name: String) {
        collectionsDao.removeCollection(name)
    }

    override suspend fun insertCollectionApp(collectionName: String, appId: Int): Boolean {
        val collection = getCollection(collectionName)

        // Check if collection exists
        if (collection != null) {
            val index = collection.size

            collectionsDao.insertCollectionApp(CollectionAppEntity(collectionName, appId, index))

            return true
        } else {
            return false
        }
    }

    override suspend fun removeCollectionApp(collectionName: String, appId: Int) {
        collectionsDao.removeCollectionApp(collectionName, appId)
    }

    override suspend fun getCollection(name: String): List<CollectionApp>? {
        val collectionWithApps = collectionsDao.getCollectionByName(name)

        return collectionWithApps?.collectionAppsDetails?.map {
            CollectionApp(it.collectionName, it.appid, it.index)
        }
    }
}
