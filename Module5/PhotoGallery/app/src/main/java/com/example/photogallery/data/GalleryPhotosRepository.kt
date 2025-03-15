package com.example.photogallery.data

import com.example.photogallery.model.RequestResult
import com.example.photogallery.model.Screenshot
import com.example.photogallery.network.GalleryApiService

interface GalleryPhotosRepository {
    suspend fun getGamePhotos(): List<List<Screenshot>>
}

class NetworkGalleryPhotosRepository(
    private val galleryApiService: GalleryApiService,
    private val gameIds: List<Int>
): GalleryPhotosRepository {
    override suspend fun getGamePhotos(): List<List<Screenshot>> {
        val responses = ArrayList<Map<String, RequestResult>>()

        for (gameId: Int in gameIds) {
            responses.add(galleryApiService.getGamePhotos(gameId))
        }

        val responseLists = ArrayList<List<Screenshot>>()

        for (response: Map<String, RequestResult> in responses) {
            responseLists.add(response.values.firstOrNull()?.data?.screenshots?: emptyList())
        }

        return responseLists
    }
}