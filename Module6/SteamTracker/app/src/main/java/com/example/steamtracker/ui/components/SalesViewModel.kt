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
import com.example.steamtracker.data.SpyRepository
import com.example.steamtracker.model.SteamSpyAppRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface SalesUiState {
    data class Success(val salesGames: List<SteamSpyAppRequest>) : SalesUiState
    data object Error : SalesUiState
    data object Loading : SalesUiState
}

class SalesViewModel(
    private val spyRepository: SpyRepository
): ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var salesUiState: SalesUiState by mutableStateOf(SalesUiState.Loading)
        private set

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
            salesUiState = SalesUiState.Loading
            salesUiState = try {
                SalesUiState.Success(spyRepository.getTopSales())
            } catch (e: IOException) {
                SalesUiState.Error
            } catch (e: HttpException) {
                SalesUiState.Error
            }
        }
    }

    // Factory companion object to allow repository to be passed to view model on creation
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
