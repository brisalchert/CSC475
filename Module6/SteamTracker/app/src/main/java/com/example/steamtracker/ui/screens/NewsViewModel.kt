package com.example.steamtracker.ui.screens

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
import com.example.steamtracker.data.SteamworksRepository
import com.example.steamtracker.model.NewsItem
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface NewsUiState {
    data class Success(val newsItems: List<List<NewsItem>>) : NewsUiState
    data object Error : NewsUiState
    data object Loading : NewsUiState
}

class NewsViewModel(
    private val steamworksRepository: SteamworksRepository
): ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var newsUiState: NewsUiState by mutableStateOf(NewsUiState.Loading)
        private set

    /**
     * Call getNews() on init so we can display status immediately.
     */
    init {
        getNews()
    }

    /**
     * Gets news games from the API Retrofit services and updates the
     * list of news games
     */
    fun getNews() {
        viewModelScope.launch {
            newsUiState = NewsUiState.Loading
            newsUiState = try {
                val newsList = mutableListOf<List<NewsItem>>()
                newsList.add(steamworksRepository.getAppNews(1245620))

                NewsUiState.Success(newsList)
            } catch (e: IOException) {
                NewsUiState.Error
            } catch (e: HttpException) {
                NewsUiState.Error
            }
        }
    }

    // Factory companion object to allow repository to be passed to view model on creation
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SteamTrackerApplication)
                val steamworksRepository = application.container.steamworksRepository
                NewsViewModel(steamworksRepository = steamworksRepository)
            }
        }
    }
}
