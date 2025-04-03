package com.example.steamtracker.fake

import com.example.steamtracker.model.SteamSpyAppRequest
import com.example.steamtracker.network.SpyApiService

interface FakeSpyApiService: SpyApiService {
    override suspend fun getFirstPage(): Map<String, SteamSpyAppRequest> {
        return FakeSteamSpyAppRequest.response
    }

    override suspend fun getAppDetails(appId: Int): SteamSpyAppRequest {
        return FakeSteamSpyAppRequest.response["gameId"]!!
    }
}
