package com.example.steamtracker.data

import com.example.steamtracker.model.SteamSpyAppRequest
import com.example.steamtracker.network.SpyApiService

interface SpyRepository {
    suspend fun getFirstPage(): List<SteamSpyAppRequest>
}

class NetworkSpyRepository(
    private val spyApiService: SpyApiService
): SpyRepository {
    /**
     * Returns the first page of Steam Spy games
     */
    override suspend fun getFirstPage(): List<SteamSpyAppRequest> {
        val response = spyApiService.getFirstPage()

        return(response.values.toList())
    }

}
