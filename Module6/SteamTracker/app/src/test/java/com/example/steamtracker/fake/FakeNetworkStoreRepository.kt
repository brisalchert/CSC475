package com.example.steamtracker.fake

import com.example.steamtracker.data.StoreRepository
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.StoreSearchRequest
import com.example.steamtracker.room.relations.FeaturedCategoryWithDetails
import com.example.steamtracker.utils.mapToAppInfoEntities
import com.example.steamtracker.utils.mapToFeaturedCategoryEntities
import com.example.steamtracker.utils.mapToSpotlightItemEntities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

class FakeNetworkStoreRepository(
): StoreRepository {
    override val allFeaturedCategories: Flow<List<FeaturedCategoryWithDetails>> =
        flowOf(
            FakeFeaturedCategoriesRequest.response.mapToFeaturedCategoryEntities().map {
                FeaturedCategoryWithDetails(
                    category = it,
                    appItems = FakeFeaturedCategoriesRequest.response.mapToAppInfoEntities(),
                    spotlightItems = FakeFeaturedCategoriesRequest.response.mapToSpotlightItemEntities()
                )
            }
        )


    /**
     * Refresh featured categories with API and update Room Database
     */
    override suspend fun refreshFeaturedCategories() {
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
