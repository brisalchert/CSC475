package com.example.steamtracker.ui.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.network.HttpException
import com.example.steamtracker.SteamTrackerApplication
import com.example.steamtracker.data.StoreRepository
import com.example.steamtracker.model.StoreSearchRequest
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val storeRepository: StoreRepository
): ViewModel() {
    // State flow for observing search results
    private val _searchResults = MutableStateFlow(StoreSearchRequest(0, emptyList()))
    val searchResults: StateFlow<StoreSearchRequest> = _searchResults

    // State flow for observing game name request results
    private val _nameFromId = MutableStateFlow("")
    val nameFromId: StateFlow<String> = _nameFromId

    // State flow for search errors
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    /**
     * Gets the search results for the given query, updating the live data
     */
    fun getSearchResults(query: String) {
        viewModelScope.launch {
            try {
                val response = storeRepository.getSearchResults(query)
                _searchResults.update { response }
            } catch(e: IOException) {
                _errorMessage.value = "Error: No Internet connection"
            } catch(e: HttpException) {
                _errorMessage.value = "Server error: ${e.message}"
            }
        }
    }

    /**
     * Clears the current search results, updating the live data
     */
    fun clearSearchResults() {
        _searchResults.update { StoreSearchRequest(0, emptyList()) }
    }

    /**
     * Clears the current search error message
     */
    fun clearError() {
        _errorMessage.value = null
    }

    /**
     * Gets the name of an app based on its App ID
     */
    fun getNameFromId(appId: Int) {
        viewModelScope.launch {
            try {
                val response = storeRepository.getAppName(appId)
                _nameFromId.update { response }
            } catch(e: okio.IOException) {
                _errorMessage.value = "Error: No Internet connection"
            } catch(e: HttpException) {
                _errorMessage.value = "Server error: ${e.message}"
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
                SearchViewModel(storeRepository = storeRepository)
            }
        }
    }
}
