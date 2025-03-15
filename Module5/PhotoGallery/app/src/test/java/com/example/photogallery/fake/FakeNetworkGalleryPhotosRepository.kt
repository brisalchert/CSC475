package com.example.photogallery.fake

import com.example.photogallery.data.GalleryPhotosRepository
import com.example.photogallery.model.RequestResult
import com.example.photogallery.model.Screenshot
import java.util.HashMap

class FakeNetworkGalleryPhotosRepository: GalleryPhotosRepository {
    override suspend fun getGamePhotos(gameID: Int): List<Screenshot> {
        return FakeDataSource.response.values.firstOrNull()?.data?.screenshots?: emptyList()
    }
}