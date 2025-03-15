package com.example.photogallery.fake

import com.example.photogallery.model.RequestResult
import com.example.photogallery.model.Screenshot
import com.example.photogallery.network.GalleryApiService
import retrofit2.http.Query

class FakeGalleryApiService: GalleryApiService {
    override suspend fun getGamePhotos(@Query("appids") gameId: Int): Map<String, RequestResult> {
        return FakeDataSource.response
    }
}