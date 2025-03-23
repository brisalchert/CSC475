package com.example.steamtracker.data

import android.util.Log
import com.example.steamtracker.model.AppInfo
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.FeaturedCategoriesRequest
import com.example.steamtracker.model.StoreSearchRequest
import com.example.steamtracker.network.StoreApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

interface StoreRepository {
    suspend fun getFeaturedGames(): List<AppInfo>
    suspend fun getFeaturedCategories(): FeaturedCategoriesRequest
    suspend fun getAppDetails(appId: Int): AppDetails?
    suspend fun getSearchResults(query: String): StoreSearchRequest
    suspend fun getAppName(appId: Int): String
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
