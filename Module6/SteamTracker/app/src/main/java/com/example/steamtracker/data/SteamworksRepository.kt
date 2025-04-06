package com.example.steamtracker.data

import com.example.steamtracker.network.SteamworksApiService
import com.example.steamtracker.room.dao.NewsAppsDao
import com.example.steamtracker.room.dao.SteamworksDao
import com.example.steamtracker.room.entities.NewsAppEntity
import com.example.steamtracker.room.entities.NewsItemEntity
import com.example.steamtracker.room.relations.AppNewsWithDetails
import com.example.steamtracker.utils.toAppNewsEntity
import com.example.steamtracker.utils.toAppNewsRequestEntity
import com.example.steamtracker.utils.toNewsItemEntities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.Instant

interface SteamworksRepository {
    val newsApps: Flow<List<Int>>

    suspend fun refreshAppNews()
    suspend fun addNewsApp(appid: Int)
    suspend fun removeNewsApp(appid: Int)
    suspend fun checkNewsApp(appId: Int): Boolean
    suspend fun getAllAppNews(): List<AppNewsWithDetails>
    suspend fun getNewsByGid(gid: String): NewsItemEntity
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
                    val now = Instant.now()
                    val twoMonthsAgo = now.minusSeconds(2 * 30 * 24 * 60 * 60L) // 60 days

                    val itemInstant = Instant.ofEpochSecond(item.date)

                    itemInstant.isAfter(twoMonthsAgo)
                }

                request.copy(appnews = request.appnews.copy(newsitems = filteredItems))
            }

            // Convert API response to Room database entities
            val appNewsRequestEntities = filteredResponseList.map { it.toAppNewsRequestEntity() }
            val appNewsEntities = filteredResponseList.map { it.toAppNewsEntity() }
            val newsItemEntities = filteredResponseList.map { it.toNewsItemEntities() }.flatten()

            // Insert into Room Database using transactions
            steamworksDao.insertAppNewsWithDetails(
                appNewsRequestEntities,
                appNewsEntities,
                newsItemEntities
            )
        }
    }

    override suspend fun addNewsApp(appid: Int) {
        newsAppsDao.insertNewsApp(NewsAppEntity(appid))
    }

    override suspend fun removeNewsApp(appid: Int) {
        newsAppsDao.deleteNewsApp(appid)
    }

    /** Allows the view model to access news items */
    override suspend fun getAllAppNews(): List<AppNewsWithDetails> {
        return steamworksDao.getAllAppNews().first()
    }

    override suspend fun checkNewsApp(appId: Int): Boolean {
        return newsAppsDao.checkForId(appId) != null
    }

    override suspend fun getNewsByGid(gid: String): NewsItemEntity {
        return steamworksDao.getNewsByGid(gid)
    }
}
