package com.example.photogallery.network

import com.example.photogallery.model.GalleryPhoto
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.http.GET

private const val BASE_URL =
    "https://android-kotlin-fun-mars-server.appspot.com"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface GalleryApiService {
    @GET("photos")
    suspend fun getPhotos(): List<GalleryPhoto>
}

object GalleryApi {
    val retrofitService: GalleryApiService by lazy {
        retrofit.create(GalleryApiService::class.java)
    }
}