package com.example.photogallery.network

import com.example.photogallery.model.RequestResult
import retrofit2.http.GET
import retrofit2.http.Query

interface GalleryApiService {
    @GET("appdetails?filters=screenshots")
    suspend fun getGamePhotos(@Query("appids") gameId: Int): Map<String, RequestResult>
}