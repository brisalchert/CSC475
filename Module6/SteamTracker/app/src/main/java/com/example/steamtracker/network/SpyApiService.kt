package com.example.steamtracker.network

import com.example.steamtracker.model.SteamSpyAppRequest
import retrofit2.http.GET

interface SpyApiService {
    @GET("api.php?request=all&page=0")
    suspend fun getFirstPage(): Map<String, SteamSpyAppRequest>
}
