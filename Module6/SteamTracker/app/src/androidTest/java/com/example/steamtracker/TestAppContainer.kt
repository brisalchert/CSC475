package com.example.steamtracker

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import androidx.work.WorkManager
import com.example.steamtracker.data.AppContainer
import com.example.steamtracker.data.AppDetailsRepository
import com.example.steamtracker.data.CollectionsRepository
import com.example.steamtracker.data.NetworkAppDetailsRepository
import com.example.steamtracker.data.NetworkCollectionsRepository
import com.example.steamtracker.data.NetworkNotificationsRepository
import com.example.steamtracker.data.NetworkPreferencesRepository
import com.example.steamtracker.data.NetworkSpyRepository
import com.example.steamtracker.data.NetworkSteamworksRepository
import com.example.steamtracker.data.NetworkStoreRepository
import com.example.steamtracker.data.NotificationsRepository
import com.example.steamtracker.data.SpyRepository
import com.example.steamtracker.data.SteamworksRepository
import com.example.steamtracker.data.StoreRepository
import com.example.steamtracker.model.FeaturedCategoriesDeserializer
import com.example.steamtracker.model.FeaturedCategoriesRequest
import com.example.steamtracker.model.RequiredAgeDeserializer
import com.example.steamtracker.model.SystemRequirements
import com.example.steamtracker.model.SystemRequirementsDeserializer
import com.example.steamtracker.network.SpyApiService
import com.example.steamtracker.network.SteamworksApiService
import com.example.steamtracker.network.StoreApiService
import com.example.steamtracker.room.AppDatabase
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

private const val PREFERENCES_NAME = "preferences"
val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)

/**
 * Testing App Container that uses a MockWebServer for fake HTTP requests, as well as
 * an in-memory Room database
 */
class TestAppContainer(
    private val application: Application,
    mockWebServer: MockWebServer,
    sslSocketFactory: SSLSocketFactory,
    trustManager: X509TrustManager
): AppContainer {
    // Initialize the database instance
    override val appDatabase: AppDatabase = Room.inMemoryDatabaseBuilder(
        application,
        AppDatabase::class.java
    ).build()

    // Initialize the WorkManager instance
    override val workManager: WorkManager by lazy {
        WorkManager.getInstance(application)
    }

    private val gson = GsonBuilder()
        .registerTypeAdapter(Int::class.java, RequiredAgeDeserializer())
        .registerTypeAdapter(SystemRequirements::class.java, SystemRequirementsDeserializer())
        .registerTypeAdapter(FeaturedCategoriesRequest::class.java, FeaturedCategoriesDeserializer())
        .create()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(
            OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustManager)
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val retrofitServiceStore = retrofit.create(StoreApiService::class.java)
    private val retrofitServiceSpy = retrofit.create(SpyApiService::class.java)
    private val retrofitServiceSteamworks = retrofit.create(SteamworksApiService::class.java)

    /**
     * Initialize the storeRepository with the fake Retrofit service
     */
    override val storeRepository: StoreRepository by lazy {
        NetworkStoreRepository(
            retrofitServiceStore,
            appDatabase.storeDao(),
            appDatabase.appDetailsDao()
        )
    }

    /**
     * Initialize the spyRepository with the fake Retrofit service
     */
    override val spyRepository: SpyRepository by lazy {
        NetworkSpyRepository(
            retrofitServiceSpy,
            appDatabase.salesDao()
        )
    }

    /**
     * Initialize the steamworksRepository with the fake Retrofit service
     */
    override val steamworksRepository: SteamworksRepository by lazy {
        NetworkSteamworksRepository(
            retrofitServiceSteamworks,
            appDatabase.steamworksDao(),
            appDatabase.newsAppsDao()
        )
    }

    /**
     * Initialize the appDetailsRepository with the database table
     */
    override val appDetailsRepository: AppDetailsRepository by lazy {
        NetworkAppDetailsRepository(
            appDatabase.appDetailsDao()
        )
    }

    /**
     * Initialize the collectionsRepository with the database table
     */
    override val collectionsRepository: CollectionsRepository by lazy {
        NetworkCollectionsRepository(
            appDatabase.collectionsDao()
        )
    }

    /**
     * Initialize the notificationsRepository with the database table
     */
    override val notificationsRepository: NotificationsRepository by lazy {
        NetworkNotificationsRepository(
            appDatabase.notificationsDao()
        )
    }

    /**
     * Initialize preferences repository
     */
    override val preferencesRepository: NetworkPreferencesRepository by lazy {
        NetworkPreferencesRepository(
            application.applicationContext.dataStore
        )
    }
}
