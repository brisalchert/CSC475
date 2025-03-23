package com.example.steamtracker.data

import android.app.Application
import androidx.room.Room
import com.example.steamtracker.model.FeaturedCategoriesDeserializer
import com.example.steamtracker.model.FeaturedCategoriesRequest
import com.example.steamtracker.model.RegularCategory
import com.example.steamtracker.model.RequiredAgeDeserializer
import com.example.steamtracker.model.SystemRequirements
import com.example.steamtracker.model.SystemRequirementsDeserializer
import com.example.steamtracker.network.SpyApiService
import com.example.steamtracker.network.SteamworksApiService
import com.example.steamtracker.network.StoreApiService
import com.example.steamtracker.room.AppDatabase
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val storeRepository: StoreRepository
    val spyRepository: SpyRepository
    val steamworksRepository: SteamworksRepository
}

/**
 * The App Container holds important data so that it can be accessed across the application.
 * In this case, the repository that provides the data is contained within.
 */
class DefaultAppContainer(private val application: Application): AppContainer {
    // Initialize the database instance
    val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "steam_tracker_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    private val steamStoreBaseUrl = "https://store.steampowered.com/api/"
    private val steamSpyBaseUrl = "https://steamspy.com/"
    private val steamworksBaseUrl = "https://api.steampowered.com/"

    /**
     * Use the Retrofit builder to build a retrofit object for Steam store requests
     */
    private val gsonStore = GsonBuilder()
        .registerTypeAdapter(Int::class.java, RequiredAgeDeserializer())
        .registerTypeAdapter(SystemRequirements::class.java, SystemRequirementsDeserializer())
        .registerTypeAdapter(FeaturedCategoriesRequest::class.java, FeaturedCategoriesDeserializer())
        .create()

    private val retrofitStore: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gsonStore))
        .baseUrl(steamStoreBaseUrl)
        .build()

    private val retrofitServiceStore: StoreApiService by lazy {
        retrofitStore.create(StoreApiService::class.java)
    }

    override val storeRepository: StoreRepository by lazy {
        NetworkStoreRepository(
            retrofitServiceStore,
            appDatabase.FeaturedCategoriesDao()
        )
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
        NetworkSpyRepository(
            retrofitServiceSpy
        )
    }

    /**
     * Use the Retrofit builder to build a retrofit object for Steamworks requests
     */
    private val gsonSteamworks = GsonBuilder().create()

    private val retrofitSteamworks: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gsonSteamworks))
        .baseUrl(steamworksBaseUrl)
        .build()

    private val retrofitServiceSteamworks: SteamworksApiService by lazy {
        retrofitSteamworks.create(SteamworksApiService::class.java)
    }

    override val steamworksRepository: SteamworksRepository by lazy {
        NetworkSteamworksRepository(
            retrofitServiceSteamworks
        )
    }
}
