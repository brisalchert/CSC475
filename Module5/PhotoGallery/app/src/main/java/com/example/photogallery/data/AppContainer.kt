package com.example.photogallery.data

import com.example.photogallery.network.GalleryApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val galleryPhotosRepository: GalleryPhotosRepository
}

class DefaultAppContainer: AppContainer {
    private val baseURL = "https://android-kotlin-fun-mars-server.appspot.com"

    /**
     * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
     */
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseURL)
        .build()

    private val retrofitService: GalleryApiService by lazy {
        retrofit.create(GalleryApiService::class.java)
    }

    override val galleryPhotosRepository: GalleryPhotosRepository by lazy {
        NetworkGalleryPhotosRepository(retrofitService)
    }
}