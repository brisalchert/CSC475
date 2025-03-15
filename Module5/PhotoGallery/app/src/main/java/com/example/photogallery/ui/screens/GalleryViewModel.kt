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
import com.example.photogallery.model.RequestResult
import com.example.photogallery.model.Screenshot
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface GalleryUiState {
    data class Success(val photos: List<List<Screenshot>>) : GalleryUiState
    data object Error : GalleryUiState
    data object Loading : GalleryUiState
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
        getGamePhotos()
    }

    /**
     * Gets gallery photos information from the gallery API Retrofit service and updates the
     * [RequestResult] [List] [MutableList].
     */
    fun getGamePhotos() {
        viewModelScope.launch {
            galleryUiState = GalleryUiState.Loading
            galleryUiState = try {
                GalleryUiState.Success(galleryPhotosRepository.getGamePhotos())
            } catch (e: IOException) {
                GalleryUiState.Error
            } catch (e: HttpException) {
                GalleryUiState.Error
            }
        }
    }

    // Factory companion object to allow repository to be passed to view model on creation
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
