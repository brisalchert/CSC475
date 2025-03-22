package com.example.steamtracker.network

import com.example.steamtracker.model.AppDetailsRequest
import com.example.steamtracker.model.FeaturedGamesRequest
import retrofit2.http.GET
import retrofit2.http.Query

interface StoreApiService {
    @GET("featured")
    suspend fun getFeaturedGames(): FeaturedGamesRequest

    @GET("appdetails?")
    suspend fun getAppDetails(@Query("appids") gameId: Int): Map<String, AppDetailsRequest>
}
