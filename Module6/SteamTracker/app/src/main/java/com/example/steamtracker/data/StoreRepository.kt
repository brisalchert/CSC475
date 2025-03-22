package com.example.steamtracker.data

import com.example.steamtracker.model.FeaturedGame
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.network.StoreApiService

interface StoreRepository {
    suspend fun getFeaturedGames(): List<FeaturedGame>
    suspend fun getAppDetails(appId: Int): AppDetails?
}

class NetworkStoreRepository(
    private val storeApiService: StoreApiService
): StoreRepository {
    /**
     * Returns a list of the Steam store's current featured games
     */
    override suspend fun getFeaturedGames(): List<FeaturedGame> {
        val response = storeApiService.getFeaturedGames()

        // Return only featured Windows games, since all games are Windows
        // compatible and other categories contain duplicates
        return response.featuredWin
    }

    /**
     * Returns a GameInfo object for the game corresponding to the
     * provided App ID, or null if no app is found
     */
    override suspend fun getAppDetails(appId: Int): AppDetails? {
        val response = storeApiService.getAppDetails(appId)

        // App ID is contained within gameInfo; outer App ID not needed
        return response["$appId"]?.appDetails
    }
}
