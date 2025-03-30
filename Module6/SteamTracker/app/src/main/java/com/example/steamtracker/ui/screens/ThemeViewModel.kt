package com.example.steamtracker.ui.screens

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.steamtracker.SteamTrackerApplication

class ThemeViewModel(
    private val application: Application
): AndroidViewModel(application) {
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("settings", Context.MODE_PRIVATE)

    private val _isDarkMode = MutableStateFlow(sharedPreferences.getBoolean("dark_mode", false))
    val isDarkMode = _isDarkMode.asStateFlow()

    fun toggleTheme() {
        val newTheme = !_isDarkMode.value
        _isDarkMode.value = newTheme
        sharedPreferences.edit(commit = true) { putBoolean("dark_mode", newTheme) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SteamTrackerApplication)
                ThemeViewModel(
                    application = application
                )
            }
        }
    }
}
