package com.example.steamtracker.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.room.util.TableInfo
import com.example.steamtracker.R
import com.example.steamtracker.model.NewsItem
import com.example.steamtracker.ui.components.NewsCard

@Composable
fun NewsScreen(
    newsUiState: NewsUiState,
    getNews: () -> Unit,
    getNameFromId: (appId: Int) -> Unit,
    nameFromId: String,
    newsAppsViewModel: ViewModel,
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
                Button(onClick = getNews) {
                    Text("Reload Page")
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
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
        stickyHeader {
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
        }
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
