package com.example.steamtracker.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.NewsNotification
import com.example.steamtracker.ui.theme.SteamTrackerTheme
import com.example.steamtracker.utils.formatUnixTimestampSeconds

@Composable
fun NewsNotificationListItem(
    newsNotification: NewsNotification,
    trackedAppsDetails: List<AppDetails?>,
    navigateNews: () -> Unit,
    onNewsSelected: (gid: String) -> Unit,
    onRemoved: (notification: NewsNotification) -> Unit,
    modifier: Modifier = Modifier
) {
    var showNotificationDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceDim
        ),
        onClick = { showNotificationDialog = true }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = formatUnixTimestampSeconds(newsNotification.timestamp / 1000),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )

                Text(
                    text = "New posts to view",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    text = "${newsNotification.newPosts.size} new posts since last visit (click to view).",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            IconButton(
                onClick = { onRemoved(newsNotification) }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove notification",
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    if (showNotificationDialog) {
        NewsNotificationDialog(
            newsNotification = newsNotification,
            trackedAppsDetails = trackedAppsDetails,
            navigateNews = navigateNews,
            onNewsSelected = onNewsSelected,
            onDismiss = { showNotificationDialog = false }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsNotificationDialog(
    newsNotification: NewsNotification,
    trackedAppsDetails: List<AppDetails?>,
    navigateNews: () -> Unit,
    onNewsSelected: (gid: String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp,
            modifier = Modifier
                .padding(vertical = 64.dp)
                .fillMaxWidth()
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
                            text = "Notification Details",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                items(items = newsNotification.newPosts.sortedByDescending { it.date }) { post ->
                    NewsCard(
                        newsItem = post,
                        appDetails = trackedAppsDetails.find {
                            it?.steamAppId == post.appid
                        },
                        navigateNews = navigateNews,
                        onNewsSelected = onNewsSelected
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewsNotificationListItemPreview() {
    SteamTrackerTheme {
        NewsNotificationListItem(
            newsNotification = NewsNotification(timestamp = 0L),
            trackedAppsDetails = listOf(),
            navigateNews = {},
            onNewsSelected = {},
            onRemoved = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NewsNotificationDialogPreview() {
    SteamTrackerTheme {
        NewsNotificationDialog(
            newsNotification = NewsNotification(timestamp = 0L),
            trackedAppsDetails = listOf(),
            navigateNews = {},
            onNewsSelected = {},
            onDismiss = {}
        )
    }
}
