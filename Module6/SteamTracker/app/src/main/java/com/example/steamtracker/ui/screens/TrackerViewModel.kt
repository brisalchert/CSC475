package com.example.steamtracker.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.steamtracker.SteamTrackerApplication
import com.example.steamtracker.data.TrackerRepository
import com.example.steamtracker.model.GamePhotosRequestResult
import com.example.steamtracker.model.Screenshot
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface TrackerUiState {
    data class Success(val photos: List<Pair<String, List<Screenshot>>>) : TrackerUiState
    data object Error : TrackerUiState
    data object Loading : TrackerUiState
}

class TrackerViewModel(
    private val trackerRepository: TrackerRepository
): ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var trackerUiState: TrackerUiState by mutableStateOf(TrackerUiState.Loading)
        private set

    /**
     * Call getGamePhotos() on init so we can display status immediately.
     */
    init {
        getGamePhotos()
    }

    /**
     * Gets game photos from the API Retrofit service and updates the
     * [GamePhotosRequestResult] [List] [MutableList].
     */
    fun getGamePhotos() {
        viewModelScope.launch {
            trackerUiState = TrackerUiState.Loading
            trackerUiState = try {
                TrackerUiState.Success(trackerRepository.getGamePhotos())
            } catch (e: IOException) {
                TrackerUiState.Error
            } catch (e: HttpException) {
                TrackerUiState.Error
            }
        }
    }

    // Factory companion object to allow repository to be passed to view model on creation
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SteamTrackerApplication)
                val trackerRepository = application.container.trackerRepository
                TrackerViewModel(trackerRepository = trackerRepository)
            }
        }
    }
}
