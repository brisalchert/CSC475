package com.example.steamtracker.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.steamtracker.room.entities.SteamSpyAppEntity
import com.example.steamtracker.room.entities.TagEntity
import com.example.steamtracker.room.relations.SteamSpyAppWithTags
import kotlinx.coroutines.flow.Flow

@Dao
interface SpyDao {
    @Query("DELETE FROM steam_spy_apps")
    suspend fun clearAllSpyApps()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: SteamSpyAppEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTags(tags: List<TagEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<SteamSpyAppEntity>)

    // Insert all entities in a single transaction
    @Transaction
    suspend fun insertAppInfoWithTags(spyEntities: List<SteamSpyAppEntity>, tagEntities: List<List<TagEntity>>) {
        insertAll(spyEntities)
        tagEntities.forEach { tagList ->
            insertTags(tagList)
        }
    }

    @Transaction
    @Query("SELECT * FROM steam_spy_apps WHERE appid = :appId")
    suspend fun getSpyInfo(appId: Int): SteamSpyAppWithTags?

    @Transaction
    @Query("SELECT * FROM steam_spy_apps")
    fun getAllGames(): Flow<List<SteamSpyAppWithTags>>

    @Transaction
    @Query("SELECT * FROM steam_spy_apps WHERE discount != 0")
    fun getTopSales(): Flow<List<SteamSpyAppWithTags>>


    @Query("SELECT MAX(lastUpdated) FROM steam_spy_apps")
    suspend fun getLastUpdatedTimestamp(): Long?
}
