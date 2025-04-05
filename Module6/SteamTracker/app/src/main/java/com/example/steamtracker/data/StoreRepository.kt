package com.example.steamtracker.data

import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.StoreSearchRequest
import com.example.steamtracker.network.StoreApiService
import com.example.steamtracker.room.dao.AppDetailsDao
import com.example.steamtracker.room.dao.StoreDao
import com.example.steamtracker.room.relations.FeaturedCategoryWithDetails
import com.example.steamtracker.utils.isDataOutdated
import com.example.steamtracker.utils.mapToAppInfoEntities
import com.example.steamtracker.utils.mapToFeaturedCategoryEntities
import com.example.steamtracker.utils.mapToSpotlightItemEntities
import com.example.steamtracker.utils.toAppDetails
import com.example.steamtracker.utils.toAppDetailsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface StoreRepository {
    val allFeaturedCategories: Flow<List<FeaturedCategoryWithDetails>>

    suspend fun refreshFeaturedCategories()
    suspend fun getAppDetails(appId: Int): AppDetails?
    suspend fun getAppDetailsFresh(appId: Int): AppDetails?
    suspend fun getSearchResults(query: String): StoreSearchRequest
    suspend fun clearFeaturedCategories()
}

class NetworkStoreRepository(
    private val storeApiService: StoreApiService,
    private val storeDao: StoreDao,
    private val appDetailsDao: AppDetailsDao
): StoreRepository {
    override val allFeaturedCategories: Flow<List<FeaturedCategoryWithDetails>> =
        storeDao.getAllFeaturedCategories()

    /**
     * Refresh featured categories with API and update Room Database
     */
    override suspend fun refreshFeaturedCategories() {
        // Get the timestamp of the current categories data
        val lastUpdated = storeDao.getLastUpdatedTimestamp()

        // Check if the data is outdated (not from today)
        if (isDataOutdated(lastUpdated)) {
            // Clear old categories data
            storeDao.clearAllFeaturedCategories()

            // Get data from the API
            val response = storeApiService.getFeaturedCategories()

            // Convert API response to Room database entities
            val categoryEntities = response.mapToFeaturedCategoryEntities().map {
                it.copy(lastUpdated = System.currentTimeMillis()) // Update timestamp
            }
            val appEntities = response.mapToAppInfoEntities()
            val spotlightEntities = response.mapToSpotlightItemEntities()

            // Insert into Room Database using transactions
            storeDao.insertFeaturedCategoryWithDetails(
                categoryEntities,
                appEntities,
                spotlightEntities
            )
        }
    }

    /**
     * Returns an AppDetails object for the game corresponding to the
     * provided App ID, or null if no app is found
     */
    override suspend fun getAppDetails(appId: Int): AppDetails? {
        // Check the database first
        val databaseResponse = appDetailsDao.getAppDetails(appId)

        if (databaseResponse != null && !isDataOutdated(databaseResponse.lastUpdated)) {
            return databaseResponse.toAppDetails()
        }

        val apiResponse = storeApiService.getAppDetails(appId)

        // Add response to the database
        val appDetailsEntity = apiResponse["$appId"]?.appDetails?.toAppDetailsEntity()

        if (appDetailsEntity != null) {
            appDetailsDao.insertAppDetails(appDetailsEntity)
        }

        return apiResponse["$appId"]?.appDetails
    }

    /**
     * Returns an AppDetails object for the game corresponding to the
     * provided App ID, or null if no app is found. Always makes an
     * API call, passing the database
     */
    override suspend fun getAppDetailsFresh(appId: Int): AppDetails? {
        val apiResponse = storeApiService.getAppDetails(appId)

        // Add response to the database
        val appDetailsEntity = apiResponse["$appId"]?.appDetails?.toAppDetailsEntity()

        if (appDetailsEntity != null) {
            appDetailsDao.insertAppDetails(appDetailsEntity)
        }

        return apiResponse["$appId"]?.appDetails
    }

    /**
     * Returns a StoreSearchRequest object based on the query provided
     */
    override suspend fun getSearchResults(query: String): StoreSearchRequest = withContext(Dispatchers.IO) {
        return@withContext storeApiService.getSearchResults(query)
    }

    /**
     * Clears all existing entries for featured categories
     */
    override suspend fun clearFeaturedCategories() {
        storeDao.clearAllFeaturedCategories()
    }
}
