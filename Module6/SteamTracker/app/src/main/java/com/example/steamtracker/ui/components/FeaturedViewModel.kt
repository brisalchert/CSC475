package com.example.steamtracker.ui.components

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asFlow
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
import com.example.steamtracker.utils.toAppInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
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
     * Call getFeaturedGames() on init so we can display status immediately.
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
            storeRepository.allFeaturedCategories.collectLatest { cachedData ->
                if (cachedData.isNotEmpty()) {
                    _featuredUiState.value = FeaturedUiState.Success(mapEntitiesToRequest(cachedData))
                }

                // Check if the data is outdated
                val isDataStale = cachedData.isEmpty() || isDataOutdated(cachedData)
                if (isDataStale) {
                    try {
                        storeRepository.refreshFeaturedCategories()
                    } catch (e: IOException) {
                        _featuredUiState.value = FeaturedUiState.Error
                    } catch (e: HttpException) {
                        _featuredUiState.value = FeaturedUiState.Error
                    }
                }
            }
        }
    }

    /**
     * Maps database entities from the Room Database to FeaturedCategoriesRequest objects
     */
    private fun mapEntitiesToRequest(entities: List<FeaturedCategoryWithDetails>): FeaturedCategoriesRequest {
        val spotlightCategories = mutableMapOf<String, Any>()

        val specials = entities.find { it.category.type == "regular" && it.category.name == "Specials" }?.let {
            RegularCategory(it.category.id, it.category.name, it.appItems?.map { it.toAppInfo() })
        }

        val comingSoon = entities.find { it.category.type == "regular" && it.category.name == "Coming Soon" }?.let {
            RegularCategory(it.category.id, it.category.name, it.appItems?.map { it.toAppInfo() })
        }

        val topSellers = entities.find { it.category.type == "regular" && it.category.name == "Top Sellers" }?.let {
            RegularCategory(it.category.id, it.category.name, it.appItems?.map { it.toAppInfo() })
        }

        val newReleases = entities.find { it.category.type == "regular" && it.category.name == "New Releases" }?.let {
            RegularCategory(it.category.id, it.category.name, it.appItems?.map { it.toAppInfo() })
        }

        val genres = entities.find { it.category.type == "static" && it.category.name == "Genres" }?.let {
            StaticCategory(it.category.id, it.category.name)
        }

        val trailerslideshow = entities.find { it.category.type == "static" && it.category.name == "Trailer Slideshow" }?.let {
            StaticCategory(it.category.id, it.category.name)
        }

        entities.filter { it.category.type == "spotlight" }.forEach {
            spotlightCategories[it.category.id] = SpotlightCategory(
                it.category.id, it.category.name, it.spotlightItems?.map { item ->
                    SpotlightItem(item.name, item.headerImage, item.body, item.url)
                }
            )
        }

        return FeaturedCategoriesRequest(
            spotlightCategories = spotlightCategories,
            specials = specials,
            comingSoon = comingSoon,
            topSellers = topSellers,
            newReleases = newReleases,
            genres = genres,
            trailerslideshow = trailerslideshow,
            status = 1
        )
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
