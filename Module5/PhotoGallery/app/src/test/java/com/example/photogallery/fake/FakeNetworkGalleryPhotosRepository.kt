package com.example.photogallery.fake

import com.example.photogallery.data.GalleryPhotosRepository
import com.example.photogallery.model.RequestResult

class FakeNetworkGalleryPhotosRepository: GalleryPhotosRepository {
    override suspend fun getGalleryPhotos(): List<RequestResult> {
        return FakeDataSource.photosList
    }
}