package com.example.photogallery.data

import com.example.photogallery.model.GalleryPhoto
import com.example.photogallery.network.GalleryApiService

interface GalleryPhotosRepository {
    suspend fun getGalleryPhotos(): List<GalleryPhoto>
}

class NetworkGalleryPhotosRepository(
    private val galleryApiService: GalleryApiService
): GalleryPhotosRepository {
    override suspend fun getGalleryPhotos(): List<GalleryPhoto> = galleryApiService.getGalleryPhotos()
}