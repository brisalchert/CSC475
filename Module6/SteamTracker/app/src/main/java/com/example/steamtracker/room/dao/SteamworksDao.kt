package com.example.steamtracker.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.steamtracker.room.entities.AppNewsEntity
import com.example.steamtracker.room.entities.AppNewsRequestEntity
import com.example.steamtracker.room.entities.NewsItemEntity
import com.example.steamtracker.room.relations.AppNewsWithDetails

@Dao
interface SteamworksDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(newsitems: List<NewsItemEntity>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppNews(appnews: List<AppNewsEntity>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppNewsRequests(appnews: List<AppNewsRequestEntity>)

    @Transaction
    @Query("SELECT * FROM app_news_requests WHERE appid = :appid")
    fun getAppNews(appid: Int): LiveData<AppNewsWithDetails>

    @Transaction
    @Query("SELECT * FROM app_news_requests")
    fun getAllAppNews(): LiveData<List<AppNewsWithDetails>>

    @Query("SELECT MAX(lastUpdated) FROM app_news_requests")
    suspend fun getLastUpdatedTimestamp(): Long?
}
