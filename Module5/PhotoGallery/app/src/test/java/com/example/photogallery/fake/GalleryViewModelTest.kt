package com.example.photogallery.fake

import com.example.photogallery.rules.TestDispatcherRule
import com.example.photogallery.ui.screens.GalleryViewModel
import com.example.photogallery.ui.screens.GalleryUiState
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class GalleryViewModelTest {
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun galleryViewModel_getGalleryPhotos_verifyGameUiStateSuccess() =
        runTest {
            val galleryViewModel = GalleryViewModel(
                galleryPhotosRepository = FakeNetworkGalleryPhotosRepository()
            )

            assertEquals(
                GalleryUiState.Success(FakeDataSource.photosList),
                galleryViewModel.galleryUiState
            )
        }
}