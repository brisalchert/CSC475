package com.example.photogallery.fake

import com.example.photogallery.data.NetworkGalleryPhotosRepository
import com.example.photogallery.model.Screenshot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class NetworkGalleryRepositoryTest {
    @Test
    fun networkGalleryPhotosRepository_getGamePhotos_verifyPhotoList() =
        runTest {
            val repository = NetworkGalleryPhotosRepository(
                galleryApiService = FakeGalleryApiService()
            )

            assertEquals(
                FakeDataSource.response.values.firstOrNull()?.data?.screenshots?: emptyList<Screenshot>(),
                repository.getGamePhotos(1)
            )
        }
}