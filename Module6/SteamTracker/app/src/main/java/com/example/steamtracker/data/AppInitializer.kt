package com.example.steamtracker.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppInitializer(
    val context: Context,
    private val container: AppContainer
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    // Checks if the app is newly installed and clears the database if it is
    suspend fun checkAndClearDatabase() {
        val isFreshInstall = sharedPreferences.getBoolean("is_fresh_install", true)

        if (isFreshInstall) {
            clearDatabase()
            markFreshInstallCompleted()
        }
    }

    // Marks the fresh-install as completed
    private fun markFreshInstallCompleted() {
        sharedPreferences.edit() {
            putBoolean("is_fresh_install", false)
        }
    }

    // Clears the database
    suspend fun clearDatabase() {
        withContext(Dispatchers.IO) {
            container.appDatabase.clearAllTables()
        }
    }
}
