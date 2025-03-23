package com.example.steamtracker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.FeaturedCategoriesRequest
import com.example.steamtracker.model.RegularCategory
import com.example.steamtracker.model.SpotlightCategory
import com.example.steamtracker.model.StoreSearchRequest
import com.example.steamtracker.network.StoreApiService
import com.example.steamtracker.room.dao.FeaturedCategoriesDao
import com.example.steamtracker.room.entities.AppInfoEntity
import com.example.steamtracker.room.entities.FeaturedCategoryEntity
import com.example.steamtracker.room.entities.SpotlightItemEntity
import com.example.steamtracker.room.relations.FeaturedCategoryWithDetails
import com.example.steamtracker.utils.toAppInfoEntityList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface StoreRepository {
    val allFeaturedCategories: Flow<List<FeaturedCategoryWithDetails>>

    suspend fun refreshFeaturedCategories()
    suspend fun getAppDetails(appId: Int): AppDetails?
    suspend fun getSearchResults(query: String): StoreSearchRequest
    suspend fun getAppName(appId: Int): String
}

class NetworkStoreRepository(
    private val storeApiService: StoreApiService,
    private val featuredCategoriesDao: FeaturedCategoriesDao
): StoreRepository {
    override val allFeaturedCategories: Flow<List<FeaturedCategoryWithDetails>> =
        featuredCategoriesDao.getAllFeaturedCategories().asFlow()

    /**
     * Refresh featured categories with API and update Room Database
     */
    override suspend fun refreshFeaturedCategories() {
        // Get data from the API
        val response = storeApiService.getFeaturedCategories()

        // Convert API response to Room database entities
        val categoryEntities = mapRequestToEntities(response)
        val appEntities = mapAppInfoToEntities(response)
        val spotlightEntities = mapSpotlightItemsToEntities(response)

        // Insert into Room Database using transactions
        featuredCategoriesDao.insertFeaturedCategories(categoryEntities)
        featuredCategoriesDao.insertAppItems(appEntities)
        featuredCategoriesDao.insertSpotlightItems(spotlightEntities)
    }

    /**
     * Maps the categories of a FeaturedCategoriesRequest to a list of database entities
     */
    fun mapRequestToEntities(request: FeaturedCategoriesRequest): List<FeaturedCategoryEntity> {
        val entities = mutableListOf<FeaturedCategoryEntity>()

        request.specials?.let {
            entities.add(FeaturedCategoryEntity(it.id, it.name, "regular", request.status))
        }
        request.comingSoon?.let {
            entities.add(FeaturedCategoryEntity(it.id, it.name, "regular", request.status))
        }
        request.topSellers?.let {
            entities.add(FeaturedCategoryEntity(it.id, it.name, "regular", request.status))
        }
        request.newReleases?.let {
            entities.add(FeaturedCategoryEntity(it.id, it.name, "regular", request.status))
        }
        request.genres?.let {
            entities.add(FeaturedCategoryEntity(it.id, it.name, "static", request.status))
        }
        request.trailerslideshow?.let {
            entities.add(FeaturedCategoryEntity(it.id, it.name, "static", request.status))
        }

        request.spotlightCategories?.forEach { (key, value) ->
            if (value is SpotlightCategory) {
                entities.add(FeaturedCategoryEntity(value.id, value.name, "spotlight", request.status))
            } else if (value is RegularCategory) {
                entities.add(FeaturedCategoryEntity(value.id, value.name, "regular", request.status))
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
        val response = storeApiService.getAppDetails(appId)

        // App ID is contained within gameInfo; outer App ID not needed
        return response["$appId"]?.appDetails
    }

    override suspend fun getAppName(appId: Int): String {
        val response = storeApiService.getAppDetails(appId)

        // Return only the app's name
        return response["$appId"]?.appDetails?.name.toString()
    }

    /**
     * Returns a StoreSearchRequest object based on the query provided
     */
    override suspend fun getSearchResults(query: String): StoreSearchRequest = withContext(Dispatchers.IO) {
        return@withContext storeApiService.getSearchResults(query)
    }
}
