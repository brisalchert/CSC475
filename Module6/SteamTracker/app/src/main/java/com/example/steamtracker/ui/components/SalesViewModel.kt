package com.example.steamtracker.ui.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.steamtracker.SteamTrackerApplication
import com.example.steamtracker.data.SpyRepository
import com.example.steamtracker.model.SteamSpyAppRequest
import com.example.steamtracker.room.relations.SteamSpyAppWithTags
import com.example.steamtracker.utils.isDataOutdated
import com.example.steamtracker.utils.toSteamSpyAppRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

sealed interface SalesUiState {
    data class Success(val salesGames: List<SteamSpyAppRequest>) : SalesUiState
    data object Error : SalesUiState
    data object Loading : SalesUiState
}

class SalesViewModel(
    private val spyRepository: SpyRepository
): ViewModel() {
    /** The mutable StateFlow that stores the status of the most recent request */
    private val _salesUiState = MutableStateFlow<SalesUiState>(SalesUiState.Loading)
    val salesUiState: StateFlow<SalesUiState> = _salesUiState.asStateFlow()

    /**
     * Call getSalesGames() on init so we can display status immediately.
     */
    init {
        getSalesGames()
    }

    /**
     * Gets sales games from the API Retrofit services and updates the
     * list of sales games
     */
    fun getSalesGames() {
        viewModelScope.launch {
            spyRepository.topSales.collectLatest { cachedData ->
                if (cachedData.isNotEmpty()) {
                    _salesUiState.value = SalesUiState.Success(mapEntitiesToRequests(cachedData))
                }

                // Check if the data is outdated
                val isDataStale = cachedData.isEmpty() || isDataOutdated(cachedData)
                if (isDataStale) {
                    try {
                        spyRepository.refreshTopSales()
                    } catch (e: IOException) {
                        SalesUiState.Error
                    } catch (e: HttpException) {
                        SalesUiState.Error
                    }
                }
            }
        }
    }

    /**
     * Maps database entities from the Room Database to SteamSpyAppRequest objects
     */
    private fun mapEntitiesToRequests(entities: List<SteamSpyAppWithTags>): List<SteamSpyAppRequest> {
        return entities.map {
            it.app.toSteamSpyAppRequest(it.tags)
        }
    }

    /**
     * Checks if the data is outdated and needs to be fetched again
     */
    private fun isDataOutdated(data: List<SteamSpyAppWithTags>): Boolean {
        val lastUpdatedTimestamp = data.maxOfOrNull { it.app.lastUpdated } ?: return true
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
                val spyRepository = application.container.spyRepository
                SalesViewModel(spyRepository = spyRepository)
            }
        }
    }
}
