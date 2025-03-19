package com.example.steamtracker

import android.app.Application
import com.example.steamtracker.data.AppContainer
import com.example.steamtracker.data.DefaultAppContainer

class SteamTrackerApplication: Application() {
    // Initialize the app container, which provides access to the repository
    // for the rest of the program
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}
