package com.example.steamtracker.network

import com.example.steamtracker.model.SteamSpyAppRequest
import retrofit2.http.GET
import retrofit2.http.Query

interface SpyApiService {
    @GET("api.php?request=all&page=0")
    suspend fun getFirstPage(): Map<String, SteamSpyAppRequest>

    @GET("api.php?request=appdetails")
    suspend fun getAppDetails(@Query("appid") appId: Int): SteamSpyAppRequest
}
