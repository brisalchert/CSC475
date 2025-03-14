package com.example.photogallery.network

import com.example.photogallery.model.GalleryPhoto
import retrofit2.http.GET

interface GalleryApiService {
    @GET("photos")
    suspend fun getGalleryPhotos(): List<GalleryPhoto>
}