package com.example.steamtracker.background

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.steamtracker.R
import com.example.steamtracker.data.CollectionsRepository
import com.example.steamtracker.data.NotificationsRepository
import com.example.steamtracker.data.StoreRepository
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.WishlistNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class WishlistNotificationWorker(
    appContext: Context,
    workerParameters: WorkerParameters,
    private val collectionsRepository: CollectionsRepository,
    private val storeRepository: StoreRepository,
    private val notificationsRepository: NotificationsRepository
): CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        try {
            // Get the wishlist
            val wishlist =
                withContext(Dispatchers.IO) {
                    collectionsRepository.getCollection("Wishlist")
                }

            // Get the current wishlist AppDetails
            val wishlistAppDetails =
                withContext(Dispatchers.IO) {
                    wishlist?.map { wishlistApp ->
                        storeRepository.getAppDetails(wishlistApp.appId)
                    }
                }


            // Get the updated wishlist AppDetails
            val updatedWishlistAppDetails =
                withContext(Dispatchers.IO) {
                    wishlist?.map { wishlistApp ->
                        storeRepository.getAppDetailsFresh(wishlistApp.appId)
                    }
                }

            // Filter out apps with unchanged price information or null AppDetails
            val newSales = updatedWishlistAppDetails?.filter { appDetails ->
                val oldAppDetails = wishlistAppDetails?.find { it?.steamAppId == appDetails?.steamAppId }

                appDetails?.priceOverview != null && oldAppDetails?.priceOverview != null &&
                        appDetails.priceOverview.final < oldAppDetails.priceOverview.final
            }?.filterNotNull() ?: emptyList()

            // Send a notification for the new posts
            if (newSales.isNotEmpty()) {
                if (ContextCompat.checkSelfPermission(
                        applicationContext, Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED) {
                    sendNotification(newSales)
                } else {
                    Log.e("NewsNotificationWorker", "Permission not granted")
                }
            }

            return Result.success()
        } catch (e: IOException) {
            // No Internet connection -- no reason to retry
            Log.e("WishlistNotificationWorker", "IOException occurred", e)
            return Result.failure()
        } catch (e: HttpException) {
            // Retry the work request, following the retry policy
            Log.e("WishlistNotificationWorker", "HttpException occurred", e)
            return Result.retry()
        }
    }

    private suspend fun sendNotification(newSales: List<AppDetails>) {
        val context = applicationContext
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (notificationManager.areNotificationsEnabled()) {
            val notification = NotificationCompat.Builder(context, getString(context, R.string.wishlist_channel))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Games on your wishlist are on sale!")
                .setContentText("There are ${newSales.size} new discounts for games on your wishlist.")
                .build()

            // Add notification to database
            notificationsRepository.insertWishlistNotification(
                WishlistNotification(
                    timestamp = System.currentTimeMillis(),
                    newSales = newSales
                )
            )

            // Send notification
            Log.d("WishlistNotificationWorker", "Sending notification with ${newSales.size} new discounts.")
            notificationManager.notify(2, notification)
        }
    }
}
