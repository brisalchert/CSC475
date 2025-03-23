package com.example.steamtracker.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.steamtracker.room.entities.SteamSpyAppEntity
import com.example.steamtracker.room.entities.TagEntity
import com.example.steamtracker.room.relations.SteamSpyAppWithTags

@Dao
interface SpyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: SteamSpyAppEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTags(tags: List<TagEntity>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<SteamSpyAppEntity>)

    @Transaction
    @Query("SELECT * FROM steam_spy_apps WHERE appid = :appid")
    suspend fun getGame(appid: Int): SteamSpyAppWithTags

    @Transaction
    @Query("SELECT * FROM steam_spy_apps")
    fun getAllGames(): LiveData<List<SteamSpyAppWithTags>>

    @Query("SELECT MAX(lastUpdated) FROM steam_spy_apps")
    suspend fun getLastUpdatedTimestamp(): Long?
}
