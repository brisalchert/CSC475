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
import com.example.steamtracker.model.SearchAppInfo
import com.example.steamtracker.model.StoreSearchRequest
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface SearchUiState {
    data class Success(val searchResults: List<SearchAppInfo>) : SearchUiState
    data object Loading : SearchUiState
    data object Error : SearchUiState
    data object NoResults : SearchUiState
}

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val storeRepository: StoreRepository
): ViewModel() {
    // State flow for UI state
    private val _searchUiState = MutableStateFlow<SearchUiState>(SearchUiState.NoResults)
    val searchUiState: StateFlow<SearchUiState> = _searchUiState.asStateFlow()

    // State flow for observing autocomplete search results
    private val _autocompleteResults = MutableStateFlow(StoreSearchRequest(0, emptyList()))
    val autocompleteResults: StateFlow<StoreSearchRequest> = _autocompleteResults

    // State flow for observing actual search results
    private val _searchResults = MutableStateFlow(StoreSearchRequest(0, emptyList()))
    val searchResults: StateFlow<StoreSearchRequest> = _searchResults

    // State flow for search errors
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    /**
     * Gets the autocomplete results for the given query, updating the live data
     */
    fun getAutocompleteResults(query: String) {
        viewModelScope.launch {
            try {
                val response = storeRepository.getSearchResults(query)
                _autocompleteResults.update { response }
            } catch (e: CancellationException) {
                throw e // Don't suppress coroutine exceptions
            } catch(e: IOException) {
                _errorMessage.value = "Error: No Internet connection"
            } catch(e: HttpException) {
                _errorMessage.value = "Server error: ${e.message}"
            }
        }
    }

    /**
     * Gets the actual search results for the given query, updating the live data
     */
    fun getSearchResults(query: String) {
        viewModelScope.launch {
            // Set to loading on new query entered
            _searchUiState.value = SearchUiState.Loading

            try {
                val response = storeRepository.getSearchResults(query)
                _searchResults.update { response }
                _searchUiState.value = SearchUiState.Success(response.items)
            } catch(e: IOException) {
                _searchUiState.value = SearchUiState.Error
            } catch(e: HttpException) {
                _searchUiState.value = SearchUiState.Error
            }
        }
    }

    /**
     * Clears the current search results, updating the live data
     */
    fun clearSearchResults() {
        _autocompleteResults.update { StoreSearchRequest(0, emptyList()) }
    }

    /**
     * Clears the current search error message
     */
    fun clearError() {
        _errorMessage.value = null
    }

    /**
     * Sorts the current search results by the given criterion
     */
    fun sortResults(by: String) {
        _searchResults.update { currentRequest ->
            val sortedItems = when (by) {
                "priceAscending" -> currentRequest.items.sortedBy { it.price?.final }
                "priceDescending" -> currentRequest.items.sortedByDescending { it.price?.final }
                "nameAscending" -> currentRequest.items.sortedBy { it.name.uppercase() }
                "nameDescending" -> currentRequest.items.sortedByDescending { it.name.uppercase() }
                "metascoreAscending" -> currentRequest.items.sortedBy { it.metascore }
                "metascoreDescending" -> currentRequest.items.sortedByDescending { it.metascore }
                else -> currentRequest.items // Keep the order unchanged if sorting criteria is unknown
            }
            currentRequest.copy(items = sortedItems)
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
