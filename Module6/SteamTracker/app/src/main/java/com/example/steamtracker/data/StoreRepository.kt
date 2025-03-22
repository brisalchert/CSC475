package com.example.steamtracker.data

import com.example.steamtracker.model.AppInfo
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.FeaturedCategoriesRequest
import com.example.steamtracker.model.StoreSearchRequest
import com.example.steamtracker.network.StoreApiService

interface StoreRepository {
    suspend fun getFeaturedGames(): List<AppInfo>
    suspend fun getFeaturedCategories(): FeaturedCategoriesRequest
    suspend fun getAppDetails(appId: Int): AppDetails?
    suspend fun getSearchResults(query: String): StoreSearchRequest
}

class NetworkStoreRepository(
    private val storeApiService: StoreApiService
): StoreRepository {
    /**
     * Returns a list of the Steam store's current featured games
     */
    override suspend fun getFeaturedGames(): List<AppInfo> {
        val response = storeApiService.getFeaturedGames()

        // Return only featured Windows games, since all games are Windows
        // compatible and other categories contain duplicates
        return response.featuredWin
    }

    /**
     * Returns a FeaturedCategoriesRequest object for the current featured categories
     */
    override suspend fun getFeaturedCategories(): FeaturedCategoriesRequest {
        return storeApiService.getFeaturedCategories()
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

    /**
     * Returns a StoreSearchRequest object based on the query provided
     */
    override suspend fun getSearchResults(query: String): StoreSearchRequest {
        return storeApiService.getSearchResults(query)
    }
}
