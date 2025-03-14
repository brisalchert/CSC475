package com.example.photogallery.data

import com.example.photogallery.model.Screenshot
import com.example.photogallery.network.GalleryApiService

interface GalleryPhotosRepository {
    suspend fun getGalleryPhotos(): List<Screenshot>
}

class NetworkGalleryPhotosRepository(
    private val galleryApiService: GalleryApiService
): GalleryPhotosRepository {
    override suspend fun getGalleryPhotos(): List<Screenshot> {
        val response = galleryApiService.getGalleryPhotos()
        return response.values.firstOrNull()?.data?.screenshots?: emptyList()
    }
}