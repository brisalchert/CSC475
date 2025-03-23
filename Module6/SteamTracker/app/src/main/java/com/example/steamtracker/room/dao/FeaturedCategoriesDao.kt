package com.example.steamtracker.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.steamtracker.room.entities.AppInfoEntity
import com.example.steamtracker.room.entities.FeaturedCategoryEntity
import com.example.steamtracker.room.entities.SpotlightItemEntity
import com.example.steamtracker.room.relations.FeaturedCategoryWithDetails

@Dao
interface FeaturedCategoriesDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeaturedCategories(categories: List<FeaturedCategoryEntity>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppItems(items: List<AppInfoEntity>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpotlightItems(items: List<SpotlightItemEntity>)

    @Transaction
    @Query("SELECT * FROM featured_categories")
    fun getAllFeaturedCategories(): LiveData<List<FeaturedCategoryWithDetails>>

    @Query("SELECT MAX(lastUpdated) FROM featured_categories")
    suspend fun getLastUpdatedTimestamp(): Long?
}
