package com.example.photogallery.fake

import com.example.photogallery.data.GalleryPhotosRepository
import com.example.photogallery.model.Screenshot

class FakeNetworkGalleryPhotosRepository: GalleryPhotosRepository {
    override suspend fun getGamePhotos(): List<Pair<String, List<Screenshot>>> {
        return ArrayList<Pair<String, List<Screenshot>>>().apply {
            FakeDataSource.gameIdsToNames.values.zip(
                listOf(FakeDataSource.response.values.firstOrNull()?.data?.screenshots?: emptyList())
            ).forEach {
                    (name, photos) -> add(Pair(name, photos))
            }
        }
    }
}