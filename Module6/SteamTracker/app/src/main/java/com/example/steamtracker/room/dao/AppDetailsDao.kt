package com.example.steamtracker.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.steamtracker.room.entities.AppDetailsEntity
import com.example.steamtracker.room.entities.AppIdAliasEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppDetails(appDetailsEntity: AppDetailsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIdMapping(aliasMapping: AppIdAliasEntity)

    @Query("SELECT * FROM app_details WHERE steamAppId = :appId")
    suspend fun getAppDetails(appId: Int): AppDetailsEntity?

    @Query("DELETE FROM app_details WHERE steamAppId = :appId")
    suspend fun deleteAppDetails(appId: Int)

    @Transaction
    @Query("SELECT * FROM app_details")
    fun getAllAppDetails(): Flow<List<AppDetailsEntity>>

    @Query("SELECT canonicalId FROM app_id_aliases WHERE aliasId = :aliasId LIMIT 1")
    suspend fun getCanonicalId(aliasId: Int): Int?
}
