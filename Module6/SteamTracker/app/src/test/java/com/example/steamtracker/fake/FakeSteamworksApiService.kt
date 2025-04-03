package com.example.steamtracker.fake

import com.example.steamtracker.model.AppNewsRequest
import com.example.steamtracker.network.SteamworksApiService

interface FakeSteamworksApiService: SteamworksApiService {
    override suspend fun getAppNews(appId: Int): AppNewsRequest {
        return FakeAppNewsRequest.response
    }
}
