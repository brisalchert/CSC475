package com.example.steamtracker.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamtracker.R
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.NewsItem
import com.example.steamtracker.ui.components.NewsCard
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@Composable
fun NewsScreen(
    newsUiState: NewsUiState,
    trackedAppsDetails: List<AppDetails?>,
    navigateNews: () -> Unit,
    onNewsSelected: (gid: String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    when (newsUiState) {
        is NewsUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is NewsUiState.Success -> NewsItemList(
            newsUiState.newsItems,
            trackedAppsDetails = trackedAppsDetails,
            navigateNews = navigateNews,
            onNewsSelected = onNewsSelected,
            modifier = modifier,
            contentPadding = contentPadding
        )
        is NewsUiState.Error -> NewsErrorScreen(
            modifier = modifier.fillMaxSize()
        )
        is NewsUiState.NoNewsApps -> Box(
            modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Add apps to your news list to see posts here!",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = 18.sp,
                    modifier = modifier.padding(16.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewsScreenPreview() {
    SteamTrackerTheme {
        NewsScreen(
            newsUiState = NewsUiState.Success(
                newsItems = listOf(listOf(NewsItem()))
            ),
            trackedAppsDetails = listOf(AppDetails()),
            navigateNews = {},
            onNewsSelected = {}
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsItemList(
    newsLists: List<List<NewsItem>>,
    trackedAppsDetails: List<AppDetails?>,
    navigateNews: () -> Unit,
    onNewsSelected: (gid: String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    if (newsLists.isEmpty()) {
        Box(
            modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.wrapContentSize(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.outlineVariant
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "No news right now,",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(4.dp),
                    )
                    Text(
                        text = "check back later!",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(4.dp),
                    )
                }
            }
        }
    } else {
        // Sort by most recent posts
        val sortedLists = newsLists.flatten().sortedByDescending { it.date }

        // Connect posts to appDetails for images
        val appDetailsMap = trackedAppsDetails.associateBy { it?.steamAppId ?: 0 }
        val newsDetailsPairs = sortedLists.map { news ->
            val appDetails = appDetailsMap[news.appid]
            news to appDetails
        }

        LazyColumn(
            modifier = modifier,
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            stickyHeader {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .shadow(8.dp, RoundedCornerShape(0.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "NEWS",
                            fontSize = 30.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                            .shadow(8.dp, RoundedCornerShape(0.dp))
                            .background(MaterialTheme.colorScheme.outlineVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Showing posts from the past 60 days",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            textAlign = TextAlign.Center
                        )
                    }
                }

            }
            items(items = newsDetailsPairs) { newsPair ->
                NewsCard(
                    newsItem = newsPair.first,
                    appDetails = newsPair.second,
                    navigateNews = navigateNews,
                    onNewsSelected = onNewsSelected,
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
fun NewsErrorScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}
