package com.example.steamtracker.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
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
            Log.d("AppInitializer", "Clearing database...")
            container.appDatabase.clearAllTables()
            Log.d("AppInitializer", "Database cleared")
            Log.d("AppInitializer", "Tables after clearing: " +
                    "${container.appDatabase.storeDao().getAllFeaturedCategories().first()}, " +
                    "${container.appDatabase.salesDao().getAllGames().first()}, " +
                    "${container.appDatabase.appDetailsDao().getAllAppDetails().first()}, " +
                    "${container.appDatabase.steamworksDao().getAllAppNews().first()}, " +
                    "${container.appDatabase.newsAppsDao().getNewsAppIds().first()}, " +
                    "${container.appDatabase.notificationsDao().getAllNewsNotifications().first()}, " +
                    "${container.appDatabase.notificationsDao().getAllWishlistNotifications().first()}"
            )
        }
    }
}
