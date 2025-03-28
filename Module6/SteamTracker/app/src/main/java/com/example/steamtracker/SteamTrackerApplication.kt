package com.example.steamtracker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.steamtracker.background.NewsWorkerFactory
import com.example.steamtracker.data.AppContainer
import com.example.steamtracker.data.AppInitializer
import com.example.steamtracker.data.DefaultAppContainer

class SteamTrackerApplication: Application() {
    /**
     * Initialize the app container and app initializer
     * to be used in the main composable
     *
     * Additionally, initialize the WorkManager and notification
     * channels for notifications
     */
    lateinit var container: AppContainer
    lateinit var appInitializer: AppInitializer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
        appInitializer = AppInitializer(this, container)

        val config = Configuration.Builder()
            .setWorkerFactory(
                NewsWorkerFactory(
                    container.steamworksRepository,
                    container.collectionsRepository,
                    container.storeRepository
                )
            )
            .build()

        WorkManager.initialize(this, config)

        // Create notification manager
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for news
        val newsChannel = NotificationChannel(
            getString(R.string.news_channel),
            getString(R.string.news_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = getString(R.string.news_channel_description)
        }
        notificationManager.createNotificationChannel(newsChannel)

        // Create notification channel for wishlist
        val wishlistChannel = NotificationChannel(
            getString(R.string.wishlist_channel),
            getString(R.string.wishlist_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = getString(R.string.wishlist_channel_description)
        }
        notificationManager.createNotificationChannel(wishlistChannel)
    }
}
