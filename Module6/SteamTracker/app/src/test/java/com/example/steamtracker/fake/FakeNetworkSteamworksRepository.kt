package com.example.steamtracker.fake

import com.example.steamtracker.data.SteamworksRepository
import com.example.steamtracker.room.entities.AppNewsEntity
import com.example.steamtracker.room.entities.AppNewsRequestEntity
import com.example.steamtracker.room.entities.NewsItemEntity
import com.example.steamtracker.room.relations.AppNewsWithDetails
import com.example.steamtracker.room.relations.AppNewsWithItems
import com.example.steamtracker.utils.toNewsItemEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeNetworkSteamworksRepository(
): SteamworksRepository {
    // List of apps specified by the user for tracking
    override val newsApps: Flow<List<Int>> =
        flowOf(listOf(0))

    override suspend fun refreshAppNews() {
    }

    override suspend fun addNewsApp(appid: Int) {
    }

    override suspend fun removeNewsApp(appid: Int) {
    }

    /** Allows the view model to access news items */
    override suspend fun getAllAppNews(): List<AppNewsWithDetails> {
        return listOf(
            AppNewsWithDetails(
                request = AppNewsRequestEntity(
                    appid = FakeAppNewsRequest.response.appnews.appid,
                    lastUpdated = 0L
                ),
                appNewsWithItems = AppNewsWithItems(
                    appnews = AppNewsEntity(
                        appid = FakeAppNewsRequest.response.appnews.appid
                    ),
                    newsitems = FakeAppNewsRequest.response.appnews.newsitems.map {
                        it.toNewsItemEntity()
                    }
                )
            )
        )
    }

    override suspend fun checkNewsApp(appId: Int): Boolean {
        return true
    }

    override suspend fun getNewsByGid(gid: String): NewsItemEntity {
        return FakeAppNewsRequest.response.appnews.newsitems.map { it.toNewsItemEntity() }.first()
    }
}
