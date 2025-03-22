package com.example.steamtracker.data

import com.example.steamtracker.model.NewsItem
import com.example.steamtracker.network.SteamworksApiService

interface SteamworksRepository {
    suspend fun getAppNews(appId: Int): List<NewsItem>
}

class NetworkSteamworksRepository(
    private val steamworksApiService: SteamworksApiService,
): SteamworksRepository {
    /**
     * Returns the three most recent news items for a given app
     */
    override suspend fun getAppNews(appId: Int): List<NewsItem> {
        return steamworksApiService.getAppNews(appId).appnews.newsitems
    }
}
