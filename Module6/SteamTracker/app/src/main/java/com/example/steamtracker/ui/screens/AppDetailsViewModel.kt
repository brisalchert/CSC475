package com.example.steamtracker.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.steamtracker.SteamTrackerApplication
import com.example.steamtracker.data.StoreRepository
import com.example.steamtracker.model.AppDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface AppDetailsUiState {
    data class Success(val appDetails: AppDetails?, val appId: Int) : AppDetailsUiState
    data class Error(val appId: Int) : AppDetailsUiState
    data object Loading : AppDetailsUiState
}

class AppDetailsViewModel(
    private val storeRepository: StoreRepository
): ViewModel() {
    // State flow for observing UI updates
    private val _appDetailsUiState = MutableStateFlow<AppDetailsUiState>(AppDetailsUiState.Loading)
    val appDetailsUiState: StateFlow<AppDetailsUiState> = _appDetailsUiState.asStateFlow()

    /**
     * Gets detailed information for an application
     */
    fun getAppDetails(appId: Int) {
        viewModelScope.launch {
            // Set UI state to loading before fetching
            _appDetailsUiState.update { AppDetailsUiState.Loading }

            try {
                val response = storeRepository.getAppDetails(appId)
                _appDetailsUiState.update { AppDetailsUiState.Success(response, appId) }
            } catch (e: IOException) {
                _appDetailsUiState.update { AppDetailsUiState.Error(appId) }
            } catch (e: HttpException) {
                _appDetailsUiState.update { AppDetailsUiState.Error(appId) }
            }
        }
    }

    /**
     * Factory companion object to allow repository to be passed to view model on creation
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SteamTrackerApplication)
                val storeRepository = application.container.storeRepository
                AppDetailsViewModel(storeRepository = storeRepository)
            }
        }
    }
}
