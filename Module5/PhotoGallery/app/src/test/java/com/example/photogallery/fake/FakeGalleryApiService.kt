package com.example.photogallery.fake

import com.example.photogallery.model.RequestResult
import com.example.photogallery.network.GalleryApiService

class FakeGalleryApiService: GalleryApiService {
    override suspend fun getGalleryPhotos(): List<RequestResult> {
        return FakeDataSource.photosList
    }
}