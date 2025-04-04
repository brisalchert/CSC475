package com.example.steamtracker.fake

import com.example.steamtracker.data.StoreRepository
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.FeaturedCategoriesRequest
import com.example.steamtracker.model.RegularCategory
import com.example.steamtracker.model.SpotlightCategory
import com.example.steamtracker.model.StoreSearchRequest
import com.example.steamtracker.room.entities.AppInfoEntity
import com.example.steamtracker.room.entities.FeaturedCategoryEntity
import com.example.steamtracker.room.entities.SpotlightItemEntity
import com.example.steamtracker.room.relations.FeaturedCategoryWithDetails
import com.example.steamtracker.utils.toAppInfoEntityList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

class FakeNetworkStoreRepository(
): StoreRepository {
    override val allFeaturedCategories: Flow<List<FeaturedCategoryWithDetails>> =
        flowOf(
            mapRequestToEntities(FakeFeaturedCategoriesRequest.response).map {
                FeaturedCategoryWithDetails(
                    category = it,
                    appItems = mapAppInfoToEntities(FakeFeaturedCategoriesRequest.response),
                    spotlightItems = mapSpotlightItemsToEntities(FakeFeaturedCategoriesRequest.response)
                )
            }
        )


    /**
     * Refresh featured categories with API and update Room Database
     */
    override suspend fun refreshFeaturedCategories() {
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
        return FakeAppDetailsRequest.response["0"]!!.appDetails
    }

    /**
     * Returns an AppDetails object for the game corresponding to the
     * provided App ID, or null if no app is found. Always makes an
     * API call, passing the database
     */
    override suspend fun getAppDetailsFresh(appId: Int): AppDetails? {
        return FakeAppDetailsRequest.response["0"]!!.appDetails
    }

    /**
     * Returns a StoreSearchRequest object based on the query provided
     */
    override suspend fun getSearchResults(query: String): StoreSearchRequest = withContext(Dispatchers.IO) {
        return@withContext FakeStoreSearchRequest.response
    }

    /**
     * Clears all existing entries for featured categories
     */
    override suspend fun clearFeaturedCategories() {
    }
}
