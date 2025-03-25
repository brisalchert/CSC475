package com.example.steamtracker.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.steamtracker.room.entities.AppInfoEntity
import com.example.steamtracker.room.entities.FeaturedCategoryEntity
import com.example.steamtracker.room.entities.SpotlightItemEntity
import com.example.steamtracker.room.relations.FeaturedCategoryWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreDao {
    @Query("DELETE FROM featured_categories")
    suspend fun clearAllFeaturedCategories()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeaturedCategories(categories: List<FeaturedCategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppItems(items: List<AppInfoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpotlightItems(items: List<SpotlightItemEntity>)

    @Transaction
    @Query("SELECT * FROM featured_categories")
    fun getAllFeaturedCategories(): Flow<List<FeaturedCategoryWithDetails>>

    @Query("SELECT MAX(lastUpdated) FROM featured_categories")
    suspend fun getLastUpdatedTimestamp(): Long?

    @Transaction
    suspend fun insertFeaturedCategoryWithDetails(
        categoryEntities: List<FeaturedCategoryEntity>,
        appInfoEntities: List<AppInfoEntity>,
        spotlightItemEntities: List<SpotlightItemEntity>
    ) {
        insertFeaturedCategories(categoryEntities)
        insertAppItems(appInfoEntities)
        insertSpotlightItems(spotlightItemEntities)
    }

}
