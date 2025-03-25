package com.example.steamtracker.ui.components

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.steamtracker.SteamTrackerApplication
import com.example.steamtracker.data.SpyRepository
import com.example.steamtracker.data.StoreRepository
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.SteamSpyAppRequest
import com.example.steamtracker.room.relations.SteamSpyAppWithTags
import com.example.steamtracker.utils.toSteamSpyAppRequest
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

sealed interface SalesUiState {
    data class Success(val salesGames: List<SteamSpyAppRequest>) : SalesUiState
    data object Error : SalesUiState
    data object Loading : SalesUiState
}

class SalesViewModel(
    private val spyRepository: SpyRepository,
    private val storeRepository: StoreRepository
): ViewModel() {
    /** The mutable StateFlow that stores the status of the most recent request */
    private val _salesUiState = MutableStateFlow<SalesUiState>(SalesUiState.Loading)
    val salesUiState: StateFlow<SalesUiState> = _salesUiState.asStateFlow()

    /** The mutable StateFlow that stores the AppDetails for discounted apps */
    private val _salesAppDetails = MutableStateFlow<List<AppDetails?>>(emptyList())
    val salesAppDetails: StateFlow<List<AppDetails?>> = _salesAppDetails.asStateFlow()

    /**
     * Call getSalesGames() on init to display status immediately
     * Call getSalesAppDetails() on init to store state of app details
     */
    init {
        getSalesGames()
        getSalesAppDetails()
    }

    /**
     * Gets sales games from the API Retrofit services and updates the
     * list of sales games
     */
    fun getSalesGames() {
        viewModelScope.launch {
            spyRepository.topSales.collect { cachedData ->
                if (cachedData.isNotEmpty()) {
                    _salesUiState.value = SalesUiState.Success(mapEntitiesToRequests(cachedData))
                }

                // Check if the data is outdated
                val isDataStale = cachedData.isEmpty() || isDataOutdated(cachedData)
                if (isDataStale) {
                    try {
                        spyRepository.refreshTopSales()
                    } catch (e: CancellationException) {
                        throw e // Don't suppress coroutine exceptions
                    } catch (e: IOException) {
                        Log.d("Debug", "${e.message}")
                        _salesUiState.value = SalesUiState.Error
                    } catch (e: HttpException) {
                        Log.d("Debug", "${e.message}")
                        _salesUiState.value = SalesUiState.Error
                    }
                }
            }
        }
    }

    fun getSalesAppDetails() {
        viewModelScope.launch {
            try {
                spyRepository.topSales.collect { salesApps ->
                    _salesAppDetails.value = salesApps.map {
                        storeRepository.getAppDetails(it.app.appid)
                    }
                }
            } catch (e: CancellationException) {
                throw e // Don't suppress coroutine exceptions
            } catch (e: IOException) {
                Log.d("Debug", "${e.message}")
                _salesUiState.value = SalesUiState.Error
            } catch (e: HttpException) {
                Log.d("Debug", "${e.message}")
                _salesUiState.value = SalesUiState.Error
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
                val storeRepository = application.container.storeRepository
                SalesViewModel(
                    spyRepository = spyRepository,
                    storeRepository = storeRepository
                )
            }
        }
    }
}
