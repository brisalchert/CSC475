package com.example.photogallery.fake

import com.example.photogallery.model.RequestResult
import com.example.photogallery.network.GalleryApiService

class FakeGalleryApiService: GalleryApiService {
    override suspend fun getGamePhotos(gameID: String): List<RequestResult> {
        return FakeDataSource.photosList
    }
}