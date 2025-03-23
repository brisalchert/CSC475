package com.example.steamtracker.ui.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.steamtracker.SteamTrackerApplication
import com.example.steamtracker.data.StoreRepository
import com.example.steamtracker.model.StoreSearchRequest
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    /**
     * Gets the search results for the given query, updating the live data
     */
    fun getSearchResults(query: String) {
        viewModelScope.launch {
            val response = storeRepository.getSearchResults(query)
            _searchResults.update { response }
        }
    }

    /**
     * Clears the current search results, updating the live data
     */
    fun clearSearchResults() {
        _searchResults.update { StoreSearchRequest(0, emptyList()) }
    }

    /**
     * Gets the name of an app based on its App ID
     */
    fun getNameFromId(appId: Int) {
        viewModelScope.launch {
            val response = storeRepository.getAppName(appId)
            _nameFromId.update { response }
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
