package com.example.steamtracker.ui.screens

import androidx.lifecycle.ViewModel
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
import com.example.steamtracker.utils.toAppNewsRequest
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
    private val workManager: WorkManager?
): ViewModel() {
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
        initWorkManager()
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
                                newsListEntities.map {
                                    it.toAppNewsRequest().appnews.newsitems
                                }
                            )
                        } else {
                            _newsUiState.value = NewsUiState.NoNewsApps
                        }
                    } catch (e: CancellationException) {
                        throw e // Don't suppress coroutine exceptions
                    } catch(e: IOException) {
                        _newsUiState.value = NewsUiState.Error
                    } catch(e: HttpException) {
                        _newsUiState.value = NewsUiState.Error
                    }
                }
            }
        }
    }

    private suspend fun fetchNewsList(): List<AppNewsWithDetails> {
        return steamworksRepository.getAllAppNews()
    }

    private fun initWorkManager() {
        workManager?.let {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                .build()

            val newsWorkRequest =
                PeriodicWorkRequestBuilder<NewsNotificationWorker>(1, TimeUnit.HOURS)
                    .setInitialDelay(10, TimeUnit.SECONDS)
                    .setConstraints(constraints)
                    .build()

            workManager
                .enqueueUniquePeriodicWork(
                    "NewsNotificationsWork",
                    ExistingPeriodicWorkPolicy.KEEP,
                    newsWorkRequest
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
                    workManager = application.container.workManager
                )
            }
        }
    }
}
