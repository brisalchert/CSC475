package com.example.steamtracker.network

import com.example.steamtracker.model.AppDetailsRequest
import com.example.steamtracker.model.FeaturedCategoriesRequest
import com.example.steamtracker.model.StoreSearchRequest
import retrofit2.http.GET
import retrofit2.http.Query

interface StoreApiService {
    @GET("featuredcategories")
    suspend fun getFeaturedCategories(): FeaturedCategoriesRequest

    @GET("appdetails?")
    suspend fun getAppDetails(@Query("appids") gameId: Int): Map<String, AppDetailsRequest>

    @GET("storesearch/?cc=us&l=en")
    suspend fun getSearchResults(@Query("term") query: String): StoreSearchRequest
}
