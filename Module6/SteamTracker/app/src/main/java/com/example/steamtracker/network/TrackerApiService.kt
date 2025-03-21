package com.example.steamtracker.network

import com.example.steamtracker.model.GamePhotosRequest
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackerApiService {
    @GET("appdetails?filters=screenshots")
    suspend fun getGamePhotos(@Query("appids") gameId: Int): Map<String, GamePhotosRequest>
}
