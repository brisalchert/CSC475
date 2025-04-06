package com.example.steamtracker.data

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

/**
 * Testing version of the app container that uses a pre-loaded database.
 */
class TestAppContainer(private val application: Application): AppContainer {
    // Initialize the test database instance
    override val appDatabase: AppDatabase by lazy {
        val dbFile = File(application.filesDir, "test-db-snapshot.db")
        if (!dbFile.exists()) {
            copyDatabaseFromAssets(application, dbFile)
        }

        Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "steam_tracker_test_database"
        ).createFromFile(dbFile)
            .fallbackToDestructiveMigration()
            .build()
    }

    // Initialize the WorkManager instance
    override val workManager: WorkManager by lazy {
        WorkManager.getInstance(application)
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
            appDatabase.storeDao(),
            appDatabase.appDetailsDao()
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
            retrofitServiceSpy,
            appDatabase.salesDao()
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

    fun copyDatabaseFromAssets(context: Context, dbFile: File) {
        val assetManager = context.assets
        val inputStream: InputStream
        val outputStream: OutputStream = FileOutputStream(dbFile)

        try {
            // Open the file from the assets folder
            inputStream = assetManager.open("test-db-snapshot.db")

            // Copy the file from assets to the app's internal storage
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }

            // Close the streams
            inputStream.close()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
