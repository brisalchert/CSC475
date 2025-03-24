package com.example.steamtracker.data

import androidx.lifecycle.asFlow
import com.example.steamtracker.model.AppNewsRequest
import com.example.steamtracker.network.SteamworksApiService
import com.example.steamtracker.room.dao.NewsAppsDao
import com.example.steamtracker.room.dao.SteamworksDao
import com.example.steamtracker.room.entities.AppNewsEntity
import com.example.steamtracker.room.entities.AppNewsRequestEntity
import com.example.steamtracker.room.entities.NewsAppEntity
import com.example.steamtracker.room.entities.NewsItemEntity
import com.example.steamtracker.room.relations.AppNewsWithDetails
import com.example.steamtracker.utils.isDataOutdated
import com.example.steamtracker.utils.toNewsItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.temporal.ChronoUnit

interface SteamworksRepository {
    val newsList: Flow<List<AppNewsWithDetails>>
    val newsApps: Flow<List<Int>>

    suspend fun refreshAppNews()
    suspend fun addNewsApp(appid: Int)
    suspend fun removeNewsApp(appid: Int)
    suspend fun getNewsAppIds(): List<Int>
}

class NetworkSteamworksRepository(
    private val steamworksApiService: SteamworksApiService,
    private val steamworksDao: SteamworksDao,
    private val newsAppsDao: NewsAppsDao
): SteamworksRepository {
    override val newsList: Flow<List<AppNewsWithDetails>> =
        steamworksDao.getAllAppNews().asFlow()

    // List of apps specified by the user for tracking
    override val newsApps: Flow<List<Int>> =
        newsAppsDao.getAllNewsApps().map { list -> list.map { it.appid } }

    override suspend fun refreshAppNews() {
        newsApps.collect { appids ->
            if (appids.isNotEmpty()) {
                // Get the timestamp of the current sales data
                val lastUpdated = steamworksDao.getLastUpdatedTimestamp()

                // Check if the data is outdated (not from today)
                if (isDataOutdated(lastUpdated)) {
                    // Get data from the API
                    val responseList = mutableListOf<AppNewsRequest>()
                    appids.forEach { app -> responseList.add(steamworksApiService.getAppNews(app)) }

                    // Filter out news from over two months ago
                    responseList.forEach { request ->
                        request.appnews.newsitems.filter { item ->
                            val now = Instant.now()
                            val twoMonthsAgo = now.minus(2, ChronoUnit.MONTHS)

                            Instant.ofEpochMilli(item.date).isAfter(twoMonthsAgo)
                        }
                    }

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
        }

    }

    override suspend fun addNewsApp(appid: Int) {
        newsAppsDao.insertNewsApp(NewsAppEntity(appid))
    }

    override suspend fun removeNewsApp(appid: Int) {
        newsAppsDao.deleteNewsApp(NewsAppEntity(appid))
    }

    override suspend fun getNewsAppIds(): List<Int> {
        return withContext(Dispatchers.IO) {
            newsAppsDao.getNewsAppIds()
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
