package com.example.photogallery.data

import com.example.photogallery.model.RequestResult
import com.example.photogallery.model.Screenshot
import com.example.photogallery.network.GalleryApiService

interface GalleryPhotosRepository {
    suspend fun getGamePhotos(): List<Pair<String, List<Screenshot>>>
}

class NetworkGalleryPhotosRepository(
    private val galleryApiService: GalleryApiService,
    private val gameIdsToNames: Map<Int, String>
): GalleryPhotosRepository {
    override suspend fun getGamePhotos(): List<Pair<String, List<Screenshot>>> {
        val responses = ArrayList<Map<String, RequestResult>>()

        for (gameId: Int in gameIdsToNames.keys) {
            responses.add(galleryApiService.getGamePhotos(gameId))
        }

        return ArrayList<Pair<String, List<Screenshot>>>().apply {
            gameIdsToNames.values.zip(
                responses.map {it.values.firstOrNull()?.data?.screenshots?: emptyList()}
            ).forEach {
                (game, photos) -> add(Pair(game, photos))
            }
        }
    }
}