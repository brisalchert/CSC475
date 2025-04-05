package com.example.steamtracker.ui.screens

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.steamtracker.SteamTrackerApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(
    private val application: Application
): AndroidViewModel(application) {
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("settings", Context.MODE_PRIVATE)

    private val _allowRecommendations = MutableStateFlow(sharedPreferences.getBoolean("allow_recommendations", true))
    val allowRecommendations = _allowRecommendations.asStateFlow()

    private val _showTopSellers = MutableStateFlow(sharedPreferences.getBoolean("show_top_sellers", true))
    val showTopSellers = _showTopSellers.asStateFlow()

    fun toggleRecommendations() {
        val newRecommendations = !_allowRecommendations.value
        _allowRecommendations.value = newRecommendations
        sharedPreferences.edit(commit = true) { putBoolean("allow_recommendations", newRecommendations) }
    }

    fun toggleTopSellers() {
        val newTopSellers = !_showTopSellers.value
        _showTopSellers.value = newTopSellers
        sharedPreferences.edit(commit = true) { putBoolean("show_top_sellers", newTopSellers) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SteamTrackerApplication)
                SettingsViewModel(
                    application = application
                )
            }
        }
    }
}
