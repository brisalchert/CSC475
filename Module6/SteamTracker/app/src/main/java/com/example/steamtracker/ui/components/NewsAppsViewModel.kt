package com.example.steamtracker.ui.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.steamtracker.SteamTrackerApplication
import com.example.steamtracker.data.SteamworksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsAppsViewModel(
    private val steamworksRepository: SteamworksRepository
): ViewModel() {
    val newsApps: Flow<List<Int>> = steamworksRepository.newsApps

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

    suspend fun checkNewsApp(appId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val appExists = steamworksRepository.checkNewsApp(appId)
            appExists
        }
    }

    // Checks (asynchronously) if an app is registered for the news list
    fun checkNewsAppAsync(appId: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val exists = checkNewsApp(appId)
            onResult(exists)
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
                NewsAppsViewModel(steamworksRepository = steamworksRepository)
            }
        }
    }
}
