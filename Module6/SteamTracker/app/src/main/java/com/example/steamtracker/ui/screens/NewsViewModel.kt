package com.example.steamtracker.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.steamtracker.SteamTrackerApplication
import com.example.steamtracker.data.SteamworksRepository
import com.example.steamtracker.model.AppNews
import com.example.steamtracker.model.AppNewsRequest
import com.example.steamtracker.model.NewsItem
import com.example.steamtracker.room.relations.AppNewsWithDetails
import com.example.steamtracker.utils.toNewsItem
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

sealed interface NewsUiState {
    data class Success(val newsItems: List<List<NewsItem>>) : NewsUiState
    data object Error : NewsUiState
    data object Loading : NewsUiState
}

class NewsViewModel(
    private val steamworksRepository: SteamworksRepository
): ViewModel() {
    /** The mutable StateFlow that stores the status of the most recent request */
    private val _newsUiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val newsUiState: StateFlow<NewsUiState> = _newsUiState.asStateFlow()

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
            steamworksRepository.newsList.collectLatest { cachedData ->
                if (cachedData.isNotEmpty()) {
                    _newsUiState.value = NewsUiState.Success(
                        mapEntitiesToRequests(cachedData).map { it.appnews.newsitems }
                    )
                }

                // Check if the data is outdated
                val isDataStale = cachedData.isEmpty() || isDataOutdated(cachedData)
                if (isDataStale) {
                    try {
                        steamworksRepository.refreshAppNews()
                    } catch (e: IOException) {
                        NewsUiState.Error
                    } catch (e: HttpException) {
                        NewsUiState.Error
                    }
                }
            }
        }
    }

    /**
     * Maps database entities from the Room Database to AppNewsRequest objects
     */
    private fun mapEntitiesToRequests(entities: List<AppNewsWithDetails>): List<AppNewsRequest> {
        val appNews = entities.map { entity ->
            val newsItems = entity.appNewsWithItems.newsitems.map { it.toNewsItem() }
            Log.d("Debug", "Fetched ${newsItems.size} news items for appid ${entity.request.appid}")
            AppNews(
                appid = newsItems.first().appid,
                newsitems = newsItems
            )
        }

        return appNews.map { news ->
            AppNewsRequest(
                appnews = news
            )
        }
    }

    /**
     * Checks if the data is outdated and needs to be fetched again
     */
    private fun isDataOutdated(data: List<AppNewsWithDetails>): Boolean {
        val lastUpdatedTimestamp = data.maxOfOrNull { it.request.lastUpdated } ?: return true
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
                val steamworksRepository = application.container.steamworksRepository
                NewsViewModel(steamworksRepository = steamworksRepository)
            }
        }
    }
}
