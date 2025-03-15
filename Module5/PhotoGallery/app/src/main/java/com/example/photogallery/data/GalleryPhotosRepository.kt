package com.example.photogallery.data

import com.example.photogallery.model.Screenshot
import com.example.photogallery.network.GalleryApiService

interface GalleryPhotosRepository {
    suspend fun getGamePhotos(gameID: Int): List<Screenshot>
}

class NetworkGalleryPhotosRepository(
    private val galleryApiService: GalleryApiService
): GalleryPhotosRepository {
    override suspend fun getGamePhotos(gameID: Int): List<Screenshot> {
        val response = galleryApiService.getGamePhotos(gameID)
        return response.values.firstOrNull()?.data?.screenshots?: emptyList()
    }
}