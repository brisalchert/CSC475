package com.example.photogallery

import android.app.Application
import com.example.photogallery.data.AppContainer
import com.example.photogallery.data.DefaultAppContainer

class PhotoGalleryApplication: Application() {
    // Initialize the app container, which provides access to the repository
    // for the rest of the program
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}