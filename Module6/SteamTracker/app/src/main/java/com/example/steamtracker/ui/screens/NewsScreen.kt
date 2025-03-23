package com.example.steamtracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.steamtracker.model.NewsItem
import com.example.steamtracker.ui.components.NewsCard
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun NewsScreen(
    newsUiState: NewsUiState,
    getNews: () -> Unit,
    getNameFromId: (appId: Int) -> Unit,
    nameFromId: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    when (newsUiState) {
        is NewsUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is NewsUiState.Success -> NewsItemList(
            newsUiState.newsItems,
            getNameFromId = getNameFromId,
            nameFromId = nameFromId,
            modifier = modifier,
            contentPadding = contentPadding
        )
        is NewsUiState.Error -> StoreErrorScreen(
            retryAction = getNews,
            modifier = modifier.fillMaxSize()
        )
    }
}

@Composable
fun NewsItemList(
    newsLists: List<List<NewsItem>>,
    getNameFromId: (Int) -> Unit,
    nameFromId: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        items(items = newsLists) { newsLists ->
            newsLists.forEach { news ->
                getNameFromId(news.appid)

                NewsCard(
                    newsItem = news,
                    appName = nameFromId,
                    modifier = modifier
                )
            }
        }
    }
}
