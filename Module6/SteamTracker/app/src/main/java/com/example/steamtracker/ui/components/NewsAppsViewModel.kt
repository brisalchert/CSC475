package com.example.steamtracker.ui.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.steamtracker.SteamTrackerApplication
import com.example.steamtracker.data.SteamworksRepository
import com.example.steamtracker.data.StoreRepository
import com.example.steamtracker.model.AppDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NewsAppsViewModel(
    private val steamworksRepository: SteamworksRepository,
    private val storeRepository: StoreRepository
): ViewModel() {
    /** Observe state of flow object from repository */
    val newsApps: StateFlow<List<Int>> =
        steamworksRepository.newsApps
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    /** The mutable StateFlow that stores the AppDetails for tracked apps */
    private val _trackedAppsDetails = MutableStateFlow<List<AppDetails?>>(emptyList())
    val trackedAppDetails: StateFlow<List<AppDetails?>> = _trackedAppsDetails.asStateFlow()

    /**
     * Call getTrackedAppsDetails on init to observe changes
     */
    init {
        getTrackedAppsDetails()
    }

    fun addNewsApp(appId: Int) {
        viewModelScope.launch {
            steamworksRepository.addNewsApp(appId)
        }
    }

    fun removeNewsApp(appId: Int) {
        viewModelScope.launch {
            steamworksRepository.removeNewsApp(appId)
        }
    }

    fun isAppTracked(appId: Int): Flow<Boolean> {
        return newsApps.map { list -> appId in list }
    }

    fun getTrackedAppsDetails() {
        viewModelScope.launch {
            newsApps.collect { newsAppsList ->
                _trackedAppsDetails.value = newsAppsList.map {
                    storeRepository.getAppDetails(it)
                }
            }
        }
    }

    /**
     * Factory companion object to allow repository to be passed to view model on creation
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY] as SteamTrackerApplication)
                val steamworksRepository = application.container.steamworksRepository
                val storeRepository = application.container.storeRepository
                NewsAppsViewModel(
                    steamworksRepository = steamworksRepository,
                    storeRepository = storeRepository
                )
            }
        }
    }
}
