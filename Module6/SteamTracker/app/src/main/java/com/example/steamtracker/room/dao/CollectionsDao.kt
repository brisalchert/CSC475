package com.example.steamtracker.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.steamtracker.room.entities.CollectionAppEntity
import com.example.steamtracker.room.entities.CollectionEntity
import com.example.steamtracker.room.relations.CollectionWithApps
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionsDao {
    @Query("DELETE FROM collections")
    suspend fun clearCollections()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollection(collection: CollectionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollectionApp(app: CollectionAppEntity)

    @Query("DELETE FROM collections WHERE name = :name")
    suspend fun removeCollection(name: String)

    @Query("DELETE FROM collection_apps WHERE collectionName = :collectionName AND appId = :appId")
    suspend fun removeCollectionApp(collectionName: String, appId: Int)

    @Transaction
    @Query("SELECT * FROM collections")
    fun getAllCollections(): Flow<List<CollectionWithApps>>

    @Transaction
    @Query("SELECT * FROM collections WHERE name = :collectionName")
    suspend fun getCollectionByName(collectionName: String): CollectionWithApps?
}
