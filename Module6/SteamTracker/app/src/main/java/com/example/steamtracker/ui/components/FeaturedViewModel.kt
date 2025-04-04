package com.example.steamtracker.ui.components

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.steamtracker.SteamTrackerApplication
import com.example.steamtracker.data.StoreRepository
import com.example.steamtracker.model.FeaturedCategoriesRequest
import com.example.steamtracker.model.RegularCategory
import com.example.steamtracker.model.SpotlightCategory
import com.example.steamtracker.model.SpotlightItem
import com.example.steamtracker.model.StaticCategory
import com.example.steamtracker.room.relations.FeaturedCategoryWithDetails
import com.example.steamtracker.utils.mapToFeaturedCategoriesRequest
import com.example.steamtracker.utils.toAppInfo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

sealed interface FeaturedUiState {
    data class Success(val featuredCategories: FeaturedCategoriesRequest) : FeaturedUiState
    data object Error : FeaturedUiState
    data object Loading : FeaturedUiState
}

class FeaturedViewModel(
    private val storeRepository: StoreRepository
): ViewModel() {
    /** The mutable StateFlow that stores the status of the most recent request */
    private val _featuredUiState = MutableStateFlow<FeaturedUiState>(FeaturedUiState.Loading)
    val featuredUiState: StateFlow<FeaturedUiState> = _featuredUiState.asStateFlow()

    /**
     * Call getFeaturedGames() on init to display status immediately
     */
    init {
        getFeaturedCategories()
    }

    /**
     * Gets detailed featured categories information from the API Retrofit service
     * and updates the list of featured games
     */
    fun getFeaturedCategories() {
        viewModelScope.launch {
            storeRepository.allFeaturedCategories.collect { cachedData ->
                if (cachedData.isNotEmpty()) {
                    _featuredUiState.value = FeaturedUiState.Success(mapToFeaturedCategoriesRequest(cachedData))
                }

                // Check if the data is outdated
                val isDataStale = cachedData.isEmpty() || isDataOutdated(cachedData)
                if (isDataStale) {
                    try {
                        storeRepository.refreshFeaturedCategories()
                    } catch (e: CancellationException) {
                        throw e // Don't suppress coroutine exceptions
                    } catch (e: IOException) {
                        Log.d("Debug", "${e.message}")
                        _featuredUiState.value = FeaturedUiState.Error
                    } catch (e: HttpException) {
                        Log.d("Debug", "${e.message}")
                        _featuredUiState.value = FeaturedUiState.Error
                    }
                }
            }
        }
    }

    /**
     * Checks if the data is outdated and needs to be fetched again
     */
    private fun isDataOutdated(data: List<FeaturedCategoryWithDetails>): Boolean {
        val lastUpdatedTimestamp = data.maxOfOrNull { it.category.lastUpdated } ?: return true
        val lastUpdatedDate = Instant.ofEpochMilli(lastUpdatedTimestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        val currentDate = LocalDate.now()
        return lastUpdatedDate.isBefore(currentDate)
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
