package com.example.photogallery.network

import com.example.photogallery.model.GalleryPhoto
import com.example.photogallery.model.Screenshot
import retrofit2.http.GET

interface GalleryApiService {
    @GET("appdetails?appids=1245620&filters=screenshots")
    suspend fun getGalleryPhotos(): Map<String, GalleryPhoto>
}