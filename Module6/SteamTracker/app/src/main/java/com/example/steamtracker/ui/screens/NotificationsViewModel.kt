package com.example.steamtracker.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.steamtracker.SteamTrackerApplication
import com.example.steamtracker.data.NotificationsRepository
import com.example.steamtracker.model.NewsNotification
import com.example.steamtracker.model.WishlistNotification
import com.example.steamtracker.utils.mapToNewsNotification
import com.example.steamtracker.utils.mapToWishlistNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

sealed interface NotificationsUiState {
    data class Success(
        val newsNotifications: List<NewsNotification>,
        val wishlistNotifications: List<WishlistNotification>
    ) : NotificationsUiState
    data object Loading : NotificationsUiState
    data object NoNotifications : NotificationsUiState
}

class NotificationsViewModel(
    private val notificationsRepository: NotificationsRepository,
): ViewModel() {
    /** The mutable StateFlow that stores the status of the notifications screen */
    private val _notificationsUiState = MutableStateFlow<NotificationsUiState>(NotificationsUiState.Loading)
    val notificationsUiState: StateFlow<NotificationsUiState> = _notificationsUiState.asStateFlow()

    /**
     * Call getAllNotifications() on init to display notifications immediately
     */
    init {
        getAllNotifications()
    }

    fun getAllNotifications() {
        viewModelScope.launch {
            // Combine flows to enable collecting both at once
            val notificationsFlow = combine(
                notificationsRepository.newsNotifications,
                notificationsRepository.wishlistNotifications
            ) { newsNotifications, wishlistNotifications ->
                Pair(newsNotifications, wishlistNotifications)
            }

            notificationsFlow.collect { (newsNotificationsEntities, wishlistNotificationsEntities) ->
                // Convert entities to model objects
                val newsNotifications = newsNotificationsEntities.map { it.mapToNewsNotification() }
                val wishlistNotifications = wishlistNotificationsEntities.map { it.mapToWishlistNotification() }

                if (newsNotifications.isNotEmpty() || wishlistNotifications.isNotEmpty()) {
                    // Update UI State
                    _notificationsUiState.value = NotificationsUiState.Success(
                        newsNotifications.sortedByDescending { it.timestamp },
                        wishlistNotifications.sortedByDescending { it.timestamp }
                    )
                } else {
                    _notificationsUiState.value = NotificationsUiState.NoNotifications
                }
            }
        }
    }

    fun deleteNewsNotification(notification: NewsNotification) {
        viewModelScope.launch {
            notificationsRepository.deleteNewsNotification(notification)
        }
    }

    fun deleteWishlistNotification(notification: WishlistNotification) {
        viewModelScope.launch {
            notificationsRepository.deleteWishlistNotification(notification)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SteamTrackerApplication)
                val notificationsRepository = application.container.notificationsRepository
                NotificationsViewModel(
                    notificationsRepository = notificationsRepository
                )
            }
        }
    }
}
