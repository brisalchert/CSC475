package com.example.photogallery.fake

import com.example.photogallery.data.NetworkGalleryPhotosRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class NetworkGalleryRepositoryTest {
    @Test
    fun networkGalleryPhotosRepository_getGamePhotos_verifyPhotoList() =
        runTest {
            val repository = NetworkGalleryPhotosRepository(
                galleryApiService = FakeGalleryApiService(),
                FakeDataSource.gameIdsToNames
            )

            assertEquals(
                listOf(
                    Pair(
                        "Game",
                        FakeDataSource.response.values.firstOrNull()?.data?.screenshots?: emptyList()
                    )
                ),
                repository.getGamePhotos()
            )
        }
}