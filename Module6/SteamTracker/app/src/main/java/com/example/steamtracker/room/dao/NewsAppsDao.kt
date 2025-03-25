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

    @Query("DELETE FROM news_apps WHERE appid = :appId")
    suspend fun deleteNewsApp(appId: Int)

    @Query("SELECT appid FROM news_apps")
    fun getNewsAppIds(): Flow<List<Int>>

    @Query("SELECT * FROM news_apps WHERE appid = :appId")
    suspend fun checkForId(appId: Int): Int?
}
