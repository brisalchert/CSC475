package com.example.steamtracker.data

import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.FeaturedCategoriesRequest
import com.example.steamtracker.model.RegularCategory
import com.example.steamtracker.model.SpotlightCategory
import com.example.steamtracker.model.StoreSearchRequest
import com.example.steamtracker.network.StoreApiService
import com.example.steamtracker.room.dao.AppDetailsDao
import com.example.steamtracker.room.dao.StoreDao
import com.example.steamtracker.room.entities.AppInfoEntity
import com.example.steamtracker.room.entities.FeaturedCategoryEntity
import com.example.steamtracker.room.entities.SpotlightItemEntity
import com.example.steamtracker.room.relations.FeaturedCategoryWithDetails
import com.example.steamtracker.utils.isDataOutdated
import com.example.steamtracker.utils.toAppDetails
import com.example.steamtracker.utils.toAppDetailsEntity
import com.example.steamtracker.utils.toAppInfoEntityList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface StoreRepository {
    val allFeaturedCategories: Flow<List<FeaturedCategoryWithDetails>>

    suspend fun refreshFeaturedCategories()
    suspend fun getAppDetails(appId: Int): AppDetails?
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
            val categoryEntities = mapRequestToEntities(response).map {
                it.copy(lastUpdated = System.currentTimeMillis()) // Update timestamp
            }
            val appEntities = mapAppInfoToEntities(response)
            val spotlightEntities = mapSpotlightItemsToEntities(response)

            // Insert into Room Database using transactions
            storeDao.insertFeaturedCategoryWithDetails(
                categoryEntities,
                appEntities,
                spotlightEntities
            )
        }
    }

    /**
     * Maps the categories of a FeaturedCategoriesRequest to a list of database entities
     */
    private fun mapRequestToEntities(request: FeaturedCategoriesRequest): List<FeaturedCategoryEntity> {
        val entities = mutableListOf<FeaturedCategoryEntity>()

        request.specials?.let {
            entities.add(FeaturedCategoryEntity(it.id, it.name, "regular", request.status, System.currentTimeMillis()))
        }
        request.comingSoon?.let {
            entities.add(FeaturedCategoryEntity(it.id, it.name, "regular", request.status, System.currentTimeMillis()))
        }
        request.topSellers?.let {
            entities.add(FeaturedCategoryEntity(it.id, it.name, "regular", request.status, System.currentTimeMillis()))
        }
        request.newReleases?.let {
            entities.add(FeaturedCategoryEntity(it.id, it.name, "regular", request.status, System.currentTimeMillis()))
        }
        request.genres?.let {
            entities.add(FeaturedCategoryEntity(it.id, it.name, "static", request.status, System.currentTimeMillis()))
        }
        request.trailerslideshow?.let {
            entities.add(FeaturedCategoryEntity(it.id, it.name, "static", request.status, System.currentTimeMillis()))
        }

        request.spotlightCategories?.forEach { (key, value) ->
            if (value is SpotlightCategory) {
                entities.add(FeaturedCategoryEntity(value.id, value.name, "spotlight", request.status, System.currentTimeMillis()))
            } else if (value is RegularCategory) {
                entities.add(FeaturedCategoryEntity(value.id, value.name, "regular", request.status, System.currentTimeMillis()))
            }
        }

        return entities
    }

    /**
     * Maps the AppInfo objects of a FeaturedCategoriesRequest to a list of database entities
     */
    private fun mapAppInfoToEntities(request: FeaturedCategoriesRequest): List<AppInfoEntity> {
        return buildList {
            request.specials?.let { addAll(it.items?.toAppInfoEntityList(it.id) ?: emptyList()) }
            request.comingSoon?.let { addAll(it.items?.toAppInfoEntityList(it.id) ?: emptyList()) }
            request.topSellers?.let { addAll(it.items?.toAppInfoEntityList(it.id) ?: emptyList()) }
            request.newReleases?.let { addAll(it.items?.toAppInfoEntityList(it.id) ?: emptyList()) }
        }
    }

    /**
     * Maps the SpotLightItem objects of a FeaturedCategoriesRequest to a list of database entities
     */
    private fun mapSpotlightItemsToEntities(request: FeaturedCategoriesRequest): List<SpotlightItemEntity> {
        val spotlightEntities = mutableListOf<SpotlightItemEntity>()

        request.spotlightCategories?.forEach { (_, value) ->
            if (value is SpotlightCategory) {
                value.items?.forEach { item ->
                    spotlightEntities.add(
                        SpotlightItemEntity(
                            name = item.name,
                            categoryId = value.id,
                            headerImage = item.headerImage,
                            body = item.body,
                            url = item.url
                        )
                    )
                }
            }
        }

        return spotlightEntities
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
