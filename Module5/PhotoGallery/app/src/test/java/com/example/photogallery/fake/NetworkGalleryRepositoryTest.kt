package com.example.photogallery.fake

import com.example.photogallery.data.NetworkGalleryPhotosRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class NetworkGalleryRepositoryTest {
    @Test
    fun networkGalleryPhotosRepository_getGalleryPhotos_verifyPhotoList() =
        runTest {
            val repository = NetworkGalleryPhotosRepository(
                galleryApiService = FakeGalleryApiService()
            )

            assertEquals(FakeDataSource.photosList, repository.getGalleryPhotos())
        }
}