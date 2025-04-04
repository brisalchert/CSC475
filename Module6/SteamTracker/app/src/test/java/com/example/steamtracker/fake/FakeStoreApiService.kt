package com.example.steamtracker.fake

import com.example.steamtracker.model.AppDetailsRequest
import com.example.steamtracker.model.FeaturedCategoriesRequest
import com.example.steamtracker.model.StoreSearchRequest
import com.example.steamtracker.network.StoreApiService

class FakeStoreApiService: StoreApiService {
    override suspend fun getAppDetails(gameId: Int): Map<String, AppDetailsRequest> {
        return FakeAppDetailsRequest.response
    }

    override suspend fun getFeaturedCategories(): FeaturedCategoriesRequest {
        return FakeFeaturedCategoriesRequest.response
    }

    override suspend fun getSearchResults(query: String): StoreSearchRequest {
        return FakeStoreSearchRequest.response
    }
}
