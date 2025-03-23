package com.example.steamtracker.ui.components

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
import com.example.steamtracker.data.StoreRepository
import com.example.steamtracker.model.AppInfo
import com.example.steamtracker.model.FeaturedCategoriesRequest
import com.example.steamtracker.model.StoreSearchRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface FeaturedUiState {
    data class SuccessFeaturedGames(val featuredGames: List<AppInfo>) : FeaturedUiState
    data class SuccessFeaturedCategories(val featuredCategories: FeaturedCategoriesRequest) : FeaturedUiState
    data object Error : FeaturedUiState
    data object Loading : FeaturedUiState
}

class FeaturedViewModel(
    private val storeRepository: StoreRepository
): ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var featuredUiState: FeaturedUiState by mutableStateOf(FeaturedUiState.Loading)
        private set

    /** The mutable state that stores the current search results for user queries */
    var searchResults: StoreSearchRequest by mutableStateOf(StoreSearchRequest(0, emptyList()))
        private set

    /**
     * Call getFeaturedGames() on init so we can display status immediately.
     */
    init {
        getFeaturedCategories()
    }

    /**
     * Gets featured games from the API Retrofit service and updates the
     * list of featured games
     */
    fun getFeaturedGames() {
        viewModelScope.launch {
            featuredUiState = FeaturedUiState.Loading
            featuredUiState = try {
                FeaturedUiState.SuccessFeaturedGames(storeRepository.getFeaturedGames())
            } catch (e: IOException) {
                FeaturedUiState.Error
            } catch (e: HttpException) {
                FeaturedUiState.Error
            }
        }
    }

    /**
     * Gets detailed featured categories information from the API Retrofit service
     * and updates the list of featured games
     */
    fun getFeaturedCategories() {
        viewModelScope.launch {
            featuredUiState = FeaturedUiState.Loading
            featuredUiState = try {
                FeaturedUiState.SuccessFeaturedCategories(storeRepository.getFeaturedCategories())
            } catch(e: IOException) {
                FeaturedUiState.Error
            } catch(e: HttpException) {
                FeaturedUiState.Error
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
                FeaturedViewModel(storeRepository = storeRepository)
            }
        }
    }
}
