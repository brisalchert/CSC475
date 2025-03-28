package com.example.steamtracker.background

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.steamtracker.data.CollectionsRepository
import com.example.steamtracker.data.SteamworksRepository
import com.example.steamtracker.data.StoreRepository

class NewsWorkerFactory(
    private val steamworksRepository: SteamworksRepository,
    private val collectionsRepository: CollectionsRepository,
    private val storeRepository: StoreRepository
): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            NewsNotificationWorker::class.java.name -> NewsNotificationWorker(
                appContext,
                workerParameters,
                steamworksRepository
            )
            WishlistNotificationWorker::class.java.name -> WishlistNotificationWorker(
                appContext,
                workerParameters,
                collectionsRepository,
                storeRepository
            )
            else -> null
        }
    }
}
