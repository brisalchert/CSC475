package com.example.steamtracker.mock

import com.example.steamtracker.data.SteamworksRepository
import com.example.steamtracker.room.relations.AppNewsWithDetails
import kotlinx.coroutines.flow.Flow

class MockSteamworksRepository(
    override val newsList: Flow<List<AppNewsWithDetails>>,
    override val newsApps: Flow<List<Int>>
) : SteamworksRepository {
    override suspend fun refreshAppNews() {
        TODO("Not yet implemented")
    }

    override suspend fun addNewsApp(appid: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun removeNewsApp(appid: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getNewsAppIds(): List<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun checkNewsApp(appId: Int): Boolean {
        TODO("Not yet implemented")
    }
}
