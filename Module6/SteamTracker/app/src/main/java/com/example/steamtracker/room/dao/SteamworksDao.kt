package com.example.steamtracker.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.steamtracker.room.entities.AppNewsEntity
import com.example.steamtracker.room.entities.AppNewsRequestEntity
import com.example.steamtracker.room.entities.NewsItemEntity
import com.example.steamtracker.room.relations.AppNewsWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface SteamworksDao {
    @Query("DELETE FROM app_news_requests")
    suspend fun clearAllAppNews()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(newsitems: List<NewsItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppNews(appnews: List<AppNewsEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppNewsRequests(appnews: List<AppNewsRequestEntity>)

    @Transaction
    @Query("SELECT * FROM app_news_requests")
    fun getAllAppNews(): Flow<List<AppNewsWithDetails>>

    @Query("SELECT * FROM news_items WHERE gid = :gid")
    suspend fun getNewsByGid(gid: String): NewsItemEntity

    @Query("SELECT MAX(lastUpdated) FROM app_news_requests")
    suspend fun getLastUpdatedTimestamp(): Long?
}
