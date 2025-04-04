package com.example.steamtracker.ui.preview

import com.example.steamtracker.data.SteamworksRepository
import com.example.steamtracker.model.NewsItem
import com.example.steamtracker.room.entities.NewsItemEntity
import com.example.steamtracker.room.relations.AppNewsWithDetails
import com.example.steamtracker.utils.toNewsItemEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeSteamworksRepository(): SteamworksRepository {
    override val newsApps: Flow<List<Int>> =
        flowOf(listOf())

    override suspend fun refreshAppNews() {
    }

    override suspend fun addNewsApp(appid: Int) {
    }

    override suspend fun removeNewsApp(appid: Int) {
    }

    override suspend fun checkNewsApp(appId: Int): Boolean {
        return false
    }

    override suspend fun getAllAppNews(): List<AppNewsWithDetails> {
        return emptyList()
    }

    override suspend fun getNewsByGid(gid: String): NewsItemEntity {
        return NewsItem().toNewsItemEntity()
    }
}
