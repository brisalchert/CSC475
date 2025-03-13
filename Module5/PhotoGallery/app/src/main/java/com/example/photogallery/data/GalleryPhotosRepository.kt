package com.example.photogallery.data

import com.example.photogallery.model.GalleryPhoto
import com.example.photogallery.network.GalleryApi

interface GalleryPhotosRepository {
    suspend fun getGalleryPhotos(): List<GalleryPhoto>
}

class NetworkGalleryPhotosRepository(): GalleryPhotosRepository {
    override suspend fun getGalleryPhotos(): List<GalleryPhoto> {
        return GalleryApi.retrofitService.getGalleryPhotos()
    }
}