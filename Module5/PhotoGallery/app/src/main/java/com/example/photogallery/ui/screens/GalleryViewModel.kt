package com.example.photogallery.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photogallery.data.NetworkGalleryPhotosRepository
import com.example.photogallery.model.GalleryPhoto
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface GalleryUiState {
    data class Success(val photos: String) : GalleryUiState
    object Error : GalleryUiState
    object Loading : GalleryUiState
}

class GalleryViewModel : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var galleryUiState: GalleryUiState by mutableStateOf(GalleryUiState.Loading)
        private set

    /**
     * Call getGalleryPhotos() on init so we can display status immediately.
     */
    init {
        getGalleryPhotos()
    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [GalleryPhoto] [List] [MutableList].
     */
    private fun getGalleryPhotos() {
        viewModelScope.launch {
            galleryUiState = GalleryUiState.Loading
            galleryUiState = try {
                val galleryPhotosRepository = NetworkGalleryPhotosRepository()
                val listResult = galleryPhotosRepository.getGalleryPhotos()
                GalleryUiState.Success(
                    "Success: ${listResult.size} photos retrieved"
                )
            } catch (e: IOException) {
                GalleryUiState.Error
            } catch (e: HttpException) {
                GalleryUiState.Error
            }
        }
    }
}
