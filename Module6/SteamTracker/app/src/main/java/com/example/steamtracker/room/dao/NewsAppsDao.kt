package com.example.steamtracker.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.steamtracker.room.entities.NewsAppEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsAppsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsApp(app: NewsAppEntity)

    @Delete
    suspend fun deleteNewsApp(app: NewsAppEntity)

    @Query("SELECT * FROM news_apps")
    fun getAllNewsApps(): Flow<List<NewsAppEntity>>

    @Query("SELECT appid FROM news_apps")
    suspend fun getNewsAppIds(): List<Int>

    @Query("SELECT * FROM news_apps WHERE appid = :appId")
    suspend fun getNewsAppById(appId: Int): Int?
}
