package com.example.photogallery.network

import com.example.photogallery.model.RequestResult
import retrofit2.http.GET

interface GalleryApiService {
    @GET("appdetails?appids=1245620&filters=screenshots")
    suspend fun getGalleryPhotos(): Map<String, RequestResult>
}