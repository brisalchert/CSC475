package com.example.steamtracker

import android.app.Application
import com.example.steamtracker.data.AppContainer
import com.example.steamtracker.data.AppInitializer
import com.example.steamtracker.data.DefaultAppContainer

class SteamTrackerApplication: Application() {
    /**
     * Initialize the app container and app initializer
     * to be used in the main composable
     */
    lateinit var container: AppContainer
    lateinit var appInitializer: AppInitializer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
        appInitializer = AppInitializer(this, container)
    }
}
