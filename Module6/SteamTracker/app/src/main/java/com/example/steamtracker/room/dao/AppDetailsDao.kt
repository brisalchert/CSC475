package com.example.steamtracker.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.steamtracker.room.entities.AppDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDetailsDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppDetails(appDetailsEntity: AppDetailsEntity)

    @Transaction
    @Query("SELECT * FROM app_details WHERE steamAppId = :appId")
    suspend fun getAppDetails(appId: Int): AppDetailsEntity?

    @Transaction
    @Query("DELETE FROM app_details WHERE steamAppId = :appId")
    suspend fun deleteAppDetails(appId: Int)

    @Transaction
    @Query("SELECT * FROM app_details")
    fun getAllAppDetails(): Flow<List<AppDetailsEntity>>
}
