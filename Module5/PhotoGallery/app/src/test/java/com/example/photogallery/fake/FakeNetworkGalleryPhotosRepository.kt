package com.example.photogallery.fake

import com.example.photogallery.data.GalleryPhotosRepository
import com.example.photogallery.model.GalleryPhoto

class FakeNetworkGalleryPhotosRepository: GalleryPhotosRepository {
    override suspend fun getGalleryPhotos(): List<GalleryPhoto> {
        return FakeDataSource.photosList
    }
}