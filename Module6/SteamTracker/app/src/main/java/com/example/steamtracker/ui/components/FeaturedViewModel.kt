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
import com.example.steamtracker.data.TrackerRepository
import com.example.steamtracker.model.FeaturedGame
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface FeaturedUiState {
    data class SuccessFeatured(val featuredGames: List<FeaturedGame>) : FeaturedUiState
    data object Error : FeaturedUiState
    data object Loading : FeaturedUiState
}

class FeaturedViewModel(
    private val trackerRepository: TrackerRepository
): ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var featuredUiState: FeaturedUiState by mutableStateOf(FeaturedUiState.Loading)
        private set

    /**
     * Call getFeaturedGames() on init so we can display status immediately.
     */
    init {
        getFeaturedGames()
    }

    /**
     * Gets featured games from the API Retrofit services and updates the
     * list of featured games
     */
    fun getFeaturedGames() {
        viewModelScope.launch {
            featuredUiState = FeaturedUiState.Loading
            featuredUiState = try {
                FeaturedUiState.SuccessFeatured(trackerRepository.getFeaturedGames())
            } catch (e: IOException) {
                FeaturedUiState.Error
            } catch (e: HttpException) {
                FeaturedUiState.Error
            }
        }
    }

    // Factory companion object to allow repository to be passed to view model on creation
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SteamTrackerApplication)
                val trackerRepository = application.container.trackerRepository
                FeaturedViewModel(trackerRepository = trackerRepository)
            }
        }
    }
}
