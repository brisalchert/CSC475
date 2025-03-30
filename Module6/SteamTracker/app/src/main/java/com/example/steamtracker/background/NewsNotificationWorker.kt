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
import com.example.steamtracker.data.SteamworksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import com.example.steamtracker.R
import com.example.steamtracker.data.NotificationsRepository
import com.example.steamtracker.model.NewsItem
import com.example.steamtracker.model.NewsNotification
import com.example.steamtracker.utils.toNewsItem

class NewsNotificationWorker(
    appContext: Context,
    workerParameters: WorkerParameters,
    private val steamworksRepository: SteamworksRepository,
    private val notificationsRepository: NotificationsRepository
): CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        try {
            // Get the current news list
            val currentNews =
                withContext(Dispatchers.IO) {
                    steamworksRepository.getAllAppNews().flatMap {
                        it.appNewsWithItems.newsitems
                    }
                }

            // Refresh the news
            withContext(Dispatchers.IO) {
                steamworksRepository.refreshAppNews()
            }

            // Get the updated news list
            val updatedNews =
                withContext(Dispatchers.IO) {
                    steamworksRepository.getAllAppNews().flatMap {
                        it.appNewsWithItems.newsitems
                    }
                }

            // Filter out existing news and convert database entities
            val newPosts = updatedNews.filter { post ->
                currentNews.find { it.gid == post.gid } == null
            }.map { it.toNewsItem() }

            // Send a notification for the new posts
            if (newPosts.isNotEmpty()) {
                if (ContextCompat.checkSelfPermission(
                        applicationContext, Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED) {
                    sendNotification(newPosts)
                } else {
                    Log.e("NewsNotificationWorker", "Permission not granted")
                }
            }

            return Result.success()
        } catch (e: IOException) {
            // No Internet connection -- no reason to retry
            Log.e("NewsNotificationWorker", "IOException occurred", e)
            return Result.failure()
        } catch (e: HttpException) {
            // Retry the work request, following the retry policy
            Log.e("NewsNotificationWorker", "HttpException occurred", e)
            return Result.retry()
        }
    }

    private suspend fun sendNotification(newPosts: List<NewsItem>) {
        val context = applicationContext
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (notificationManager.areNotificationsEnabled()) {
            val notification = NotificationCompat.Builder(context, getString(context, R.string.news_channel))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("New posts for tracked games!")
                .setContentText("You have ${newPosts.size} new posts to read.")
                .build()

            // Add notification to database
            notificationsRepository.insertNewsNotification(
                NewsNotification(
                    timestamp = System.currentTimeMillis(),
                    newPosts = newPosts
                )
            )

            // Send notification
            Log.d("NewsNotificationWorker", "Sending notification with ${newPosts.size} new posts.")
            notificationManager.notify(1, notification)
        }
    }
}
