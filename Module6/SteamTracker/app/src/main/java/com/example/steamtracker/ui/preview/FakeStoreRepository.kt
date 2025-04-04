package com.example.steamtracker.ui.preview

import com.example.steamtracker.data.StoreRepository
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.StoreSearchRequest
import com.example.steamtracker.room.relations.FeaturedCategoryWithDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeStoreRepository(): StoreRepository {
    override val allFeaturedCategories: Flow<List<FeaturedCategoryWithDetails>> =
        flowOf(listOf())

    override suspend fun refreshFeaturedCategories() {
    }

    override suspend fun getAppDetails(appId: Int): AppDetails? {
        return null
    }

    override suspend fun getAppDetailsFresh(appId: Int): AppDetails? {
        return null
    }

    override suspend fun getSearchResults(query: String): StoreSearchRequest {
        return StoreSearchRequest()
    }

    override suspend fun clearFeaturedCategories() {
    }
}
