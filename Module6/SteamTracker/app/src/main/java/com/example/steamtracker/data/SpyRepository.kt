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
    /**
     * Get the first page of results from SteamSpy if necessary
     */
    suspend fun checkFirstPage() {
        if (firstPageResponse.isEmpty()) {
            firstPageResponse = spyApiService.getFirstPage().values.toList()
        }
    }

    /**
     * Get discounted games from the first page of SteamSpy results if necessary
     */
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
     * Returns the first page of SteamSpy games
     */
    override suspend fun getFirstPage(): List<SteamSpyAppRequest> {
        checkFirstPage()

        return firstPageResponse
    }

    /**
     * Returns the top games on sale from SteamSpy
     */
    override suspend fun getTopSales(): List<SteamSpyAppRequest> {
        checkSortedSales()

        return sortedSales
    }
}
