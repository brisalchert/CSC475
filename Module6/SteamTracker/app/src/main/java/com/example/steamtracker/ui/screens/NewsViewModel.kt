package com.example.steamtracker.ui.screens

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.steamtracker.SteamTrackerApplication
import com.example.steamtracker.background.NewsNotificationWorker
import com.example.steamtracker.data.SteamworksRepository
import com.example.steamtracker.model.AppNews
import com.example.steamtracker.model.AppNewsRequest
import com.example.steamtracker.model.NewsItem
import com.example.steamtracker.room.relations.AppNewsWithDetails
import com.example.steamtracker.utils.toNewsItem
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

sealed interface NewsUiState {
    data class Success(val newsItems: List<List<NewsItem>>) : NewsUiState
    data object Loading : NewsUiState
    data object Error : NewsUiState
    data object NoNewsApps : NewsUiState
}

class NewsViewModel(
    private val steamworksRepository: SteamworksRepository,
    private val application: Application
): AndroidViewModel(application) {
    /** Observe state of flow object from repository */
    val newsApps: StateFlow<List<Int>> =
        steamworksRepository.newsApps
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    /** The mutable StateFlow that stores the status of the most recent request */
    private val _newsUiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val newsUiState: StateFlow<NewsUiState> = _newsUiState.asStateFlow()

    /**
     * Call observeTrackedApps() on init to display status immediately
     * Also set up the work request for notifications
     */
    init {
        observeTrackedApps()

        // Set up notifications periodic background work
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .build()

        val newsWorkRequest =
            PeriodicWorkRequestBuilder<NewsNotificationWorker>(12, TimeUnit.HOURS)
                .setInitialDelay(5, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .build()

        WorkManager
            .getInstance(application)
            .enqueueUniquePeriodicWork(
                "NewsNotificationsWork",
                ExistingPeriodicWorkPolicy.REPLACE,
                newsWorkRequest
            )
    }

    /**
     * Observes the list of tracked apps and updates the UI if the list changes
     */
    private fun observeTrackedApps() {
        viewModelScope.launch {
            newsApps.collect { newsAppsList ->
                // Set UI to NoNewsApps state if there are no apps in the list
                if (newsAppsList.isEmpty()) {
                    _newsUiState.value = NewsUiState.NoNewsApps
                } else {
                    try {
                        // Wait for network request to complete
                        withContext(Dispatchers.IO) {
                            steamworksRepository.refreshAppNews()
                        }

                        // Get the updated news list
                        val newsListEntities = fetchNewsList()

                        // Only proceed if newsListEntities is not empty
                        if (newsListEntities.isNotEmpty()) {
                            _newsUiState.value = NewsUiState.Success(
                                mapEntitiesToRequests(newsListEntities).map {
                                    it.appnews.newsitems
                                }
                            )
                        } else {
                            _newsUiState.value = NewsUiState.NoNewsApps
                        }
                    } catch (e: CancellationException) {
                        throw e // Don't suppress coroutine exceptions
                    } catch(e: IOException) {
                        Log.d("Debug", "${e.message}")
                        _newsUiState.value = NewsUiState.Error
                    } catch(e: HttpException) {
                        Log.d("Debug", "${e.message}")
                        _newsUiState.value = NewsUiState.Error
                    }
                }
            }
        }
    }

    private suspend fun fetchNewsList(): List<AppNewsWithDetails> {
        return steamworksRepository.getAllAppNews()
    }

    /**
     * Maps database entities from the Room Database to AppNewsRequest objects
     */
    private fun mapEntitiesToRequests(entities: List<AppNewsWithDetails>): List<AppNewsRequest> {
        val appNews = entities.mapNotNull { entity ->
            val newsItems = entity.appNewsWithItems.newsitems.map { it.toNewsItem() }

            if (newsItems.isEmpty()) {
                Log.d("Debug", "Skipping appid ${entity.request.appid} because it has no news items.")
                return@mapNotNull null
            }

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
     * Factory companion object to allow repository to be passed to view model on creation
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SteamTrackerApplication)
                val steamworksRepository = application.container.steamworksRepository
                NewsViewModel(
                    steamworksRepository = steamworksRepository,
                    application = application
                )
            }
        }
    }
}
