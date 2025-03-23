package com.example.steamtracker.data

import android.util.Log
import androidx.lifecycle.asFlow
import com.example.steamtracker.model.AppNewsRequest
import com.example.steamtracker.network.SteamworksApiService
import com.example.steamtracker.room.dao.SteamworksDao
import com.example.steamtracker.room.entities.AppNewsEntity
import com.example.steamtracker.room.entities.AppNewsRequestEntity
import com.example.steamtracker.room.entities.NewsItemEntity
import com.example.steamtracker.room.relations.AppNewsWithDetails
import com.example.steamtracker.utils.isDataOutdated
import com.example.steamtracker.utils.toNewsItemEntity
import kotlinx.coroutines.flow.Flow

interface SteamworksRepository {
    val newsList: Flow<List<AppNewsWithDetails>>

    suspend fun refreshAppNews()
}

class NetworkSteamworksRepository(
    private val steamworksApiService: SteamworksApiService,
    private val steamworksDao: SteamworksDao,
    private val appList: List<Int> = listOf(1245620)
): SteamworksRepository {
    override val newsList: Flow<List<AppNewsWithDetails>> =
        steamworksDao.getAllAppNews().asFlow()

    override suspend fun refreshAppNews() {
        // Get the timestamp of the current sales data
        val lastUpdated = steamworksDao.getLastUpdatedTimestamp()

        // Check if the data is outdated (not from today)
        if (isDataOutdated(lastUpdated)) {
            // Get data from the API
            val responseList = mutableListOf<AppNewsRequest>()
            appList.forEach { responseList.add(steamworksApiService.getAppNews(it)) }

            // Convert API response to Room database entities
            val appNewsRequestEntities = mapRequestsToEntities(responseList)
            val appNewsEntities = mapAppNewsToEntities(responseList)
            val newsItemEntities = mapNewsItemsToEntities(responseList).flatten()

            // Insert into Room Database using transactions
            steamworksDao.insertAppNewsRequests(appNewsRequestEntities)
            steamworksDao.insertAppNews(appNewsEntities)
            steamworksDao.insertNews(newsItemEntities)
        }
    }

    /**
     * Maps the AppNewsRequest objects of a list of AppNewsRequests to a list of database entities
     */
    private fun mapRequestsToEntities(requests: List<AppNewsRequest>): List<AppNewsRequestEntity> {
        return requests.map { request ->
            AppNewsRequestEntity(
                appid = request.appnews.appid,
                lastUpdated = System.currentTimeMillis()
            )
        }
    }

    /**
     * Maps the AppNews objects of a list of AppNewsRequests to a list of database entities
     */
    private fun mapAppNewsToEntities(requests: List<AppNewsRequest>): List<AppNewsEntity> {
        return requests.map { request ->
            AppNewsEntity(
                appid = request.appnews.appid
            )
        }
    }

    /**
     * Maps the NewsItem lists of a list of AppNewsRequests to a list of database entities
     */
    private fun mapNewsItemsToEntities(requests: List<AppNewsRequest>): List<List<NewsItemEntity>> {
        val newsLists = mutableListOf<List<NewsItemEntity>>()

        requests.forEach { request ->
            newsLists.add(
                request.appnews.newsitems.map { it.toNewsItemEntity() }
            )
        }

        return newsLists
    }
}
