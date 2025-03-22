package com.example.steamtracker.data

import com.example.steamtracker.model.SteamSpyAppRequest
import com.example.steamtracker.network.SpyApiService

interface SpyRepository {
    suspend fun getFirstPage(): List<SteamSpyAppRequest>
    suspend fun getTopSales(): List<SteamSpyAppRequest>
}

class NetworkSpyRepository(
    private val spyApiService: SpyApiService,
    private var firstPageResponse: List<SteamSpyAppRequest> = emptyList(),
    private var sortedSales: List<SteamSpyAppRequest> = emptyList()
): SpyRepository {
    suspend fun checkFirstPage() {
        if (firstPageResponse.isEmpty()) {
            firstPageResponse = spyApiService.getFirstPage().values.toList()
        }
    }

    suspend fun checkSortedSales() {
        checkFirstPage()

        if (sortedSales.isEmpty()) {
            sortedSales = firstPageResponse.map { it.copy() } // Create a new list as a copy
            sortedSales = sortedSales
                .filter { it.discount != "0" }
                .sortedByDescending { it.discount }
        }
    }

    /**
     * Returns the first page of Steam Spy games
     */
    override suspend fun getFirstPage(): List<SteamSpyAppRequest> {
        checkFirstPage()

        return firstPageResponse
    }

    override suspend fun getTopSales(): List<SteamSpyAppRequest> {
        checkSortedSales()

        return sortedSales
    }
}
