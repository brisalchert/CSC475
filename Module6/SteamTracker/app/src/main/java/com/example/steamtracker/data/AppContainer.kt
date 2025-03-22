package com.example.steamtracker.data

import com.example.steamtracker.model.RequiredAgeDeserializer
import com.example.steamtracker.model.SystemRequirements
import com.example.steamtracker.model.SystemRequirementsDeserializer
import com.example.steamtracker.network.SpyApiService
import com.example.steamtracker.network.StoreApiService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val storeRepository: StoreRepository
    val spyRepository: SpyRepository
}

/**
 * The App Container holds important data so that it can be accessed across the application.
 * In this case, the repository that provides the data is contained within.
 */
class DefaultAppContainer: AppContainer {
    private val steamStoreBaseUrl = "https://store.steampowered.com/api/"
    private val steamSpyBaseUrl = "https://steamspy.com/"

    /**
     * Use the Retrofit builder to build a retrofit object for Steam store requests
     */
    private val gsonStore = GsonBuilder()
        .registerTypeAdapter(Int::class.java, RequiredAgeDeserializer())
        .registerTypeAdapter(SystemRequirements::class.java, SystemRequirementsDeserializer())
        .create()

    private val retrofitStore: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gsonStore))
        .baseUrl(steamStoreBaseUrl)
        .build()

    private val retrofitServiceStore: StoreApiService by lazy {
        retrofitStore.create(StoreApiService::class.java)
    }

    override val storeRepository: StoreRepository by lazy {
        NetworkStoreRepository(retrofitServiceStore)
    }

    /**
     * Use the Retrofit builder to build a retrofit object for SteamSpy requests
     */
    private val gsonSpy = GsonBuilder().create()

    private val retrofitSpy: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gsonSpy))
        .baseUrl(steamSpyBaseUrl)
        .build()

    private val retrofitServiceSpy: SpyApiService by lazy {
        retrofitSpy.create(SpyApiService::class.java)
    }

    override val spyRepository: SpyRepository by lazy {
        NetworkSpyRepository(retrofitServiceSpy)
    }

    // TODO: Add Room database for local storage
}
