package com.example.steamtracker.data

import com.example.steamtracker.model.RequiredAgeDeserializer
import com.example.steamtracker.model.SystemRequirements
import com.example.steamtracker.model.SystemRequirementsDeserializer
import com.example.steamtracker.network.TrackerApiService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val trackerRepository: TrackerRepository
}

/**
 * The App Container holds important data so that it can be accessed across the application.
 * In this case, the repository that provides the data is contained within.
 */
class DefaultAppContainer: AppContainer {
    private val baseURL = "https://store.steampowered.com/api/"

    /**
     * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
     */
    private val gson = GsonBuilder()
        .registerTypeAdapter(Int::class.java, RequiredAgeDeserializer())
        .registerTypeAdapter(SystemRequirements::class.java, SystemRequirementsDeserializer())
        .create()

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(baseURL)
        .build()

    private val retrofitService: TrackerApiService by lazy {
        retrofit.create(TrackerApiService::class.java)
    }

    override val trackerRepository: TrackerRepository by lazy {
        NetworkTrackerRepository(retrofitService)
    }
}
