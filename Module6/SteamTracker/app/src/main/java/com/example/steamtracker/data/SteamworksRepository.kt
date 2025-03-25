package com.example.steamtracker.data

import com.example.steamtracker.model.AppNewsRequest
import com.example.steamtracker.network.SteamworksApiService
import com.example.steamtracker.room.dao.NewsAppsDao
import com.example.steamtracker.room.dao.SteamworksDao
import com.example.steamtracker.room.entities.AppNewsEntity
import com.example.steamtracker.room.entities.AppNewsRequestEntity
import com.example.steamtracker.room.entities.NewsAppEntity
import com.example.steamtracker.room.entities.NewsItemEntity
import com.example.steamtracker.room.relations.AppNewsWithDetails
import com.example.steamtracker.utils.toNewsItemEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

interface SteamworksRepository {
    val newsApps: Flow<List<Int>>

    suspend fun refreshAppNews()
    suspend fun addNewsApp(appid: Int)
    suspend fun removeNewsApp(appid: Int)
    suspend fun checkNewsApp(appId: Int): Boolean
    fun getAllAppNews(): Flow<List<AppNewsWithDetails>>
}

class NetworkSteamworksRepository(
    private val steamworksApiService: SteamworksApiService,
    private val steamworksDao: SteamworksDao,
    private val newsAppsDao: NewsAppsDao
): SteamworksRepository {
    // List of apps specified by the user for tracking
    override val newsApps: Flow<List<Int>> =
        newsAppsDao.getNewsAppIds()

    override suspend fun refreshAppNews() {
        // Clear previous news from the database
        steamworksDao.clearAllAppNews()

        val appids = newsApps.first()

        if (appids.isNotEmpty()) {
            // Get data from the API
            val responseList = appids.map { app ->
                steamworksApiService.getAppNews(app)
            }

            // Filter out news from over two months ago
            val filteredResponseList = responseList.map { request ->
                val filteredItems = request.appnews.newsitems.filter { item ->
                    val now = LocalDateTime.now()
                    val twoMonthsAgo = now.minusMonths(2)

                    val itemDateTime = Instant.ofEpochMilli(item.date)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()

                    itemDateTime.isAfter(twoMonthsAgo)
                }

                request.copy(appnews = request.appnews.copy(newsitems = filteredItems))
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

    override suspend fun addNewsApp(appid: Int) {
        newsAppsDao.insertNewsApp(NewsAppEntity(appid))
    }

    override suspend fun removeNewsApp(appid: Int) {
        newsAppsDao.deleteNewsApp(NewsAppEntity(appid))
    }

    /** Allows the view model to access news items */
    override fun getAllAppNews(): Flow<List<AppNewsWithDetails>> {
        return steamworksDao.getAllAppNews()
    }

    override suspend fun checkNewsApp(appId: Int): Boolean {
        return newsAppsDao.checkForId(appId) != null
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
