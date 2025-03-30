package com.example.steamtracker.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.NewsNotification
import com.example.steamtracker.model.WishlistNotification
import com.example.steamtracker.ui.components.NewsNotificationListItem
import com.example.steamtracker.ui.components.WishlistNotificationListItem
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@Composable
fun NotificationsScreen(
    notificationsUiState: NotificationsUiState,
    trackedAppsDetails: List<AppDetails?>,
    navigateNews: () -> Unit,
    onNewsSelected: (gid: String) -> Unit,
    navigateApp: () -> Unit,
    onAppSelect: (appId: Int) -> Unit,
    onNewsRemoved: (notification: NewsNotification) -> Unit,
    onWishlistRemoved: (notification: WishlistNotification) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .shadow(8.dp, RoundedCornerShape(0.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "NOTIFICATIONS",
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
        }

        when (notificationsUiState) {
            is NotificationsUiState.Success -> NotificationsList(
                newsNotifications = notificationsUiState.newsNotifications,
                wishlistNotifications = notificationsUiState.wishlistNotifications,
                trackedAppsDetails = trackedAppsDetails,
                navigateNews = navigateNews,
                onNewsSelected = onNewsSelected,
                navigateApp = navigateApp,
                onAppSelect = onAppSelect,
                onNewsRemoved = onNewsRemoved,
                onWishlistRemoved = onWishlistRemoved
            )
            is NotificationsUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
            is NotificationsUiState.NoNotifications -> {
                Box(
                    modifier = modifier
                        .padding(12.dp)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        elevation = CardDefaults.cardElevation(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceDim
                        )
                    ) {
                        Text(
                            text = "No notifications to view!",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationsScreenPreview() {
    SteamTrackerTheme {
        NotificationsScreen(
            notificationsUiState = NotificationsUiState.Success(
                listOf(
                    NewsNotification(timestamp = 1743523932399L)
                ),
                listOf(
                    WishlistNotification(timestamp = 1742523932399L)
                )
            ),
            trackedAppsDetails = listOf(),
            navigateNews = {},
            onNewsSelected = {},
            navigateApp = {},
            onAppSelect = {},
            onNewsRemoved = {},
            onWishlistRemoved = {}
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotificationsList(
    newsNotifications: List<NewsNotification>,
    wishlistNotifications: List<WishlistNotification>,
    trackedAppsDetails: List<AppDetails?>,
    navigateNews: () -> Unit,
    onNewsSelected: (gid: String) -> Unit,
    navigateApp: () -> Unit,
    onAppSelect: (appId: Int) -> Unit,
    onNewsRemoved: (notification: NewsNotification) -> Unit,
    onWishlistRemoved: (notification: WishlistNotification) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .shadow(8.dp, RoundedCornerShape(0.dp))
                        .background(MaterialTheme.colorScheme.outlineVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "News",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                }
            }

            items(items = newsNotifications) { notification ->
                NewsNotificationListItem(
                    newsNotification = notification,
                    trackedAppsDetails = trackedAppsDetails,
                    navigateNews = navigateNews,
                    onNewsSelected = onNewsSelected,
                    onRemoved = onNewsRemoved
                )
            }

            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .shadow(8.dp, RoundedCornerShape(0.dp))
                        .background(MaterialTheme.colorScheme.outlineVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Wishlist Sales",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                }
            }

            items(items = wishlistNotifications) { notification ->
                WishlistNotificationListItem(
                    wishlistNotification = notification,
                    navigateApp = navigateApp,
                    onAppSelect = onAppSelect,
                    onRemoved = onWishlistRemoved,
                )
            }
        }
    }
}
