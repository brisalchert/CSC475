package com.example.photogallery.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.photogallery.PhotoGalleryApplication
import com.example.photogallery.data.GalleryPhotosRepository
import com.example.photogallery.model.GalleryPhoto
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface GalleryUiState {
    data class Success(val photos: String) : GalleryUiState
    object Error : GalleryUiState
    object Loading : GalleryUiState
}

class GalleryViewModel(
    private val galleryPhotosRepository: GalleryPhotosRepository
) : ViewModel() {
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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PhotoGalleryApplication)
                val galleryPhotosRepository = application.container.galleryPhotosRepository
                GalleryViewModel(galleryPhotosRepository = galleryPhotosRepository)
            }
        }
    }
}
