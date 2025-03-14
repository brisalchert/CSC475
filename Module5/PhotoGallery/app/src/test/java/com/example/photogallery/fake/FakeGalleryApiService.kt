package com.example.photogallery.fake

import com.example.photogallery.model.GalleryPhoto
import com.example.photogallery.network.GalleryApiService

class FakeGalleryApiService: GalleryApiService {
    override suspend fun getGalleryPhotos(): List<GalleryPhoto> {
        return FakeDataSource.photosList
    }
}