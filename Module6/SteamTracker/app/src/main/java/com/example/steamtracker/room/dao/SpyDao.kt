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
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: SteamSpyAppEntity)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTags(tags: List<TagEntity>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<SteamSpyAppEntity>)

    @Transaction
    @Query("SELECT * FROM steam_spy_apps")
    fun getAllGames(): Flow<List<SteamSpyAppWithTags>>

    @Query("SELECT MAX(lastUpdated) FROM steam_spy_apps")
    suspend fun getLastUpdatedTimestamp(): Long?
}
