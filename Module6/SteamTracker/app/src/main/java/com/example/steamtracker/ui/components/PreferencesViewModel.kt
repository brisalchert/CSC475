package com.example.steamtracker.ui.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.steamtracker.SteamTrackerApplication
import com.example.steamtracker.data.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PreferencesViewModel(
    private val preferencesRepository: PreferencesRepository
): ViewModel() {
    /** StateFlow variables for observing changes to genres and tags */
    private val _favoriteGenres = MutableStateFlow<Set<String>>(emptySet())
    val favoriteGenres: StateFlow<Set<String>> = _favoriteGenres

    private val _favoriteTags = MutableStateFlow<Set<String>>(emptySet())
    val favoriteTags: StateFlow<Set<String>> = _favoriteTags

    init {
        observeGenres()
        observeTags()
    }

    private fun observeGenres() {
        viewModelScope.launch {
            preferencesRepository.getFavoriteGenres().collect {
                _favoriteGenres.value = it
            }
        }
    }

    private fun observeTags() {
        viewModelScope.launch {
            preferencesRepository.getFavoriteTags().collect {
                _favoriteTags.value = it
            }
        }
    }

    fun addFavoriteGenre(genre: String) {
        viewModelScope.launch {
            val currentFavorites = favoriteGenres.first().toMutableSet()

            currentFavorites.add(genre)

            preferencesRepository.saveFavoriteGenres(currentFavorites)
        }
    }

    fun addFavoriteTag(tag: String) {
        viewModelScope.launch {
            val currentFavorites = favoriteTags.first().toMutableSet()

            currentFavorites.add(tag)

            preferencesRepository.saveFavoriteTags(currentFavorites)
        }
    }

    fun removeFavoriteGenre(genre: String) {
        viewModelScope.launch {
            val currentFavorites = favoriteGenres.first().toMutableSet()

            currentFavorites.remove(genre)

            preferencesRepository.saveFavoriteGenres(currentFavorites)
        }
    }

    fun removeFavoriteTag(tag: String) {
        viewModelScope.launch {
            val currentFavorites = favoriteTags.first().toMutableSet()

            currentFavorites.remove(tag)

            preferencesRepository.saveFavoriteTags(currentFavorites)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SteamTrackerApplication)
                val preferencesRepository = application.container.preferencesRepository
                PreferencesViewModel(
                    preferencesRepository = preferencesRepository
                )
            }
        }
    }
}
