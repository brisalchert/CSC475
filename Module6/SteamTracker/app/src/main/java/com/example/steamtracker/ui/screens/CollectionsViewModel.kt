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
import com.example.steamtracker.background.WishlistNotificationWorker
import com.example.steamtracker.data.CollectionsRepository
import com.example.steamtracker.data.StoreRepository
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.CollectionApp
import com.example.steamtracker.room.relations.CollectionWithApps
import com.example.steamtracker.utils.mapCollectionEntitiesToCollections
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

sealed interface CollectionsUiState {
    data class Success(val collections: Map<String, List<CollectionApp>>) : CollectionsUiState
    data object Loading : CollectionsUiState
    data object Error : CollectionsUiState
    data object NoCollections : CollectionsUiState
}

class CollectionsViewModel(
    private val storeRepository: StoreRepository,
    private val collectionsRepository: CollectionsRepository,
    private val workManager: WorkManager?
): ViewModel() {
    /** Observe state of flow object from repository */
    val allCollections: StateFlow<List<CollectionWithApps>> =
        collectionsRepository.allCollections
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    /** The mutable StateFlow that stores the status of the collections screen */
    private val _collectionsUiState = MutableStateFlow<CollectionsUiState>(CollectionsUiState.Loading)
    val collectionsUiState: StateFlow<CollectionsUiState> = _collectionsUiState.asStateFlow()

    /** The mutable StateFlow that stores the AppDetails of apps in collections */
    private val _collectionsAppDetails = MutableStateFlow<List<AppDetails?>>(emptyList())
    val collectionsAppsDetails: StateFlow<List<AppDetails?>> = _collectionsAppDetails.asStateFlow()

    /** StateFlow for the last selected collections screen */
    private val _currentCollection = MutableStateFlow<Pair<String, List<CollectionApp>>>(Pair("", emptyList()))
    val currentCollection: StateFlow<Pair<String, List<CollectionApp>>> = _currentCollection.asStateFlow()

    /**
     * Call getAllCollections() on init to display collections immediately
     * Also set up the work request for wishlist notifications
     */
    init {
        getAllCollections()

        // Set up notifications periodic background work
        initWorkManager()
    }

    /**
     * Function for setting the current selection displayed
     */
    fun setCollection(collection: Pair<String, List<CollectionApp>>) {
        viewModelScope.launch {
            _currentCollection.value = collection
        }
    }

    fun addCollection(name: String) {
        viewModelScope.launch {
            collectionsRepository.insertCollection(name)
        }
    }

    fun removeCollection(name: String) {
        viewModelScope.launch {
            collectionsRepository.removeCollection(name)
        }
    }

    fun addCollectionApp(collectionName: String, appId: Int) {
        viewModelScope.launch {
            collectionsRepository.insertCollectionApp(collectionName, appId)
        }
    }

    fun removeCollectionApp(collectionName: String, appId: Int) {
        viewModelScope.launch {
            collectionsRepository.removeCollectionApp(collectionName, appId)
        }
    }

    fun isInCollection(collectionName: String, appId: Int): Flow<Boolean> {
        return allCollections.map { collections ->
            val wishlist = mapCollectionEntitiesToCollections(collections)[collectionName]
            wishlist?.any { it.appId == appId } == true
        }
    }

    fun getCollectionContents(collectionName: String): Flow<List<CollectionApp>> {
        return allCollections.map { collections ->
            mapCollectionEntitiesToCollections(collections)[collectionName] ?: emptyList()
        }
    }

    fun getAllCollections() {
        viewModelScope.launch {
            collectionsRepository.allCollections.collect { collections ->
                val collections = mapCollectionEntitiesToCollections(collections)

                // Create Wish list if it does not exist
                if (!collections.contains("Wishlist")) {
                    addCollection("Wishlist")
                }

                // Create Favorites if it does not exist
                if (!collections.contains("Favorites")) {
                    addCollection("Favorites")
                }

                if (collections.isNotEmpty()) {
                    try {
                        // Get AppDetails for all apps in collections
                        _collectionsAppDetails.value = collections.flatMap { entry ->
                                entry.value.map {
                                    storeRepository.getAppDetails(it.appId)
                                }
                            }

                        // Update UI State
                        _collectionsUiState.value = CollectionsUiState.Success(collections)
                    } catch (e: CancellationException) {
                        throw e // Don't suppress coroutine exceptions
                    } catch (e: IOException) {
                        _collectionsUiState.value = CollectionsUiState.Error
                    } catch (e: HttpException) {
                        _collectionsUiState.value = CollectionsUiState.Error
                    }
                } else {
                    _collectionsUiState.value = CollectionsUiState.NoCollections
                }
            }
        }
    }

    private fun initWorkManager() {
        workManager?.let {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                .build()

            val wishlistWorkRequest =
                PeriodicWorkRequestBuilder<WishlistNotificationWorker>(12, TimeUnit.HOURS)
                    .setInitialDelay(10, TimeUnit.SECONDS)
                    .setConstraints(constraints)
                    .build()

            workManager
                .enqueueUniquePeriodicWork(
                    "WishlistNotificationsWork",
                    ExistingPeriodicWorkPolicy.KEEP,
                    wishlistWorkRequest
                )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SteamTrackerApplication)
                val storeRepository = application.container.storeRepository
                val collectionsRepository = application.container.collectionsRepository
                CollectionsViewModel(
                    storeRepository = storeRepository,
                    collectionsRepository = collectionsRepository,
                    workManager = application.container.workManager
                )
            }
        }
    }
}
