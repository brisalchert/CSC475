package com.example.steamtracker.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.steamtracker.model.AppDetails
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface AppDetailsUiState {
    data class SuccessAppDetails(val appDetails: AppDetails?) : AppDetailsUiState
    data object Error : AppDetailsUiState
    data object Loading : AppDetailsUiState
}

class AppDetailsViewModel(
    private val trackerRepository: TrackerRepository
): ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var appDetailsUiState: AppDetailsUiState by mutableStateOf(AppDetailsUiState.Loading)
        private set
    var appId: Int by mutableIntStateOf(0)
        private set

    /**
     * Gets featured games from the API Retrofit services and updates the
     * list of featured games
     */
    fun getAppDetails() {
        viewModelScope.launch {
            appDetailsUiState = AppDetailsUiState.Loading
            appDetailsUiState = try {
                AppDetailsUiState.SuccessAppDetails(trackerRepository.getAppDetails(appId))
            } catch (e: IOException) {
                AppDetailsUiState.Error
            } catch (e: HttpException) {
                AppDetailsUiState.Error
            }
        }
    }

    /**
     * Sets the App ID to a new ID
     */
    fun setAppDetailsId(appId: Int) {
        this.appId = appId
    }

    // Factory companion object to allow repository to be passed to view model on creation
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SteamTrackerApplication)
                val trackerRepository = application.container.trackerRepository
                AppDetailsViewModel(trackerRepository = trackerRepository)
            }
        }
    }
}
