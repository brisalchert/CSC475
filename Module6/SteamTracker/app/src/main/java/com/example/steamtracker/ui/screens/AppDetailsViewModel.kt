package com.example.steamtracker.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.steamtracker.SteamTrackerApplication
import com.example.steamtracker.data.AppDetailsRepository
import com.example.steamtracker.data.SpyRepository
import com.example.steamtracker.data.StoreRepository
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.SteamSpyAppRequest
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface AppDetailsUiState {
    data class Success(
        val appDetails: AppDetails?,
        val appSpyInfo: SteamSpyAppRequest,
        val appId: Int
    ) : AppDetailsUiState
    data class Error(val appId: Int) : AppDetailsUiState
    data object Loading : AppDetailsUiState
}

class AppDetailsViewModel(
    private val storeRepository: StoreRepository,
    private val spyRepository: SpyRepository,
    private val appDetailsRepository: AppDetailsRepository
): ViewModel() {
    // State flow for observing UI updates
    private val _appDetailsUiState = MutableStateFlow<AppDetailsUiState>(AppDetailsUiState.Loading)
    val appDetailsUiState: StateFlow<AppDetailsUiState> = _appDetailsUiState.asStateFlow()

    /**
     * Gets detailed information for an application
     */
    fun getAppDetails(appId: Int) {
        viewModelScope.launch {
            // Check database for app details
            val appDetails = appDetailsRepository.getAppDetails(appId)

            if (appDetails != null) {
                try {
                    val spyInfo = spyRepository.getSpyAppInfo(appId)

                    _appDetailsUiState.update { AppDetailsUiState.Success(appDetails, spyInfo, appId) }

                    return@launch
                } catch (e: CancellationException) {
                    throw e // Don't suppress coroutine exceptions
                } catch (e: IOException) {
                    _appDetailsUiState.update { AppDetailsUiState.Error(appId) }
                } catch (e: HttpException) {
                    _appDetailsUiState.update { AppDetailsUiState.Error(appId) }
                }
            }

            // Set UI state to loading before fetching
            _appDetailsUiState.update { AppDetailsUiState.Loading }

            try {
                val storeResponse = storeRepository.getAppDetails(appId)
                val spyResponse = spyRepository.getSpyAppInfo(appId)
                _appDetailsUiState.update { AppDetailsUiState.Success(storeResponse, spyResponse, appId) }
            } catch (e: CancellationException) {
                throw e // Don't suppress coroutine exceptions
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
                val spyRepository = application.container.spyRepository
                val appDetailsRepository = application.container.appDetailsRepository
                AppDetailsViewModel(
                    storeRepository = storeRepository,
                    spyRepository = spyRepository,
                    appDetailsRepository = appDetailsRepository
                )
            }
        }
    }
}
