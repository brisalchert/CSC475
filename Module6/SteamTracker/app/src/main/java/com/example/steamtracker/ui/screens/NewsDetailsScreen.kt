package com.example.steamtracker.ui.screens

import android.text.util.Linkify
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.steamtracker.R
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.NewsItem
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@Composable
fun NewsDetailsScreen(
    news: NewsItem,
    trackedAppsDetails: List<AppDetails?>,
    modifier: Modifier = Modifier
) {
    val appDetails = trackedAppsDetails.find { it?.steamAppId == news.appid }

    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        item {
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

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .shadow(8.dp, RoundedCornerShape(0.dp))
                    .background(MaterialTheme.colorScheme.surfaceDim),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = appDetails?.name.toString(),
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }
        }

        item {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(appDetails?.headerImage)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = "Image for ${appDetails?.name}",
                contentScale = ContentScale.Crop,
                modifier = modifier.fillMaxWidth()
            )
        }

        val textAsHtml = news.contents.replace('[', '<').replace(']', '>')

        item {
            Card(
                modifier = Modifier.padding(8.dp),
                elevation = CardDefaults.cardElevation(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceDim
                )
            ) {
                Text(
                    text = AnnotatedString.Companion.fromHtml(htmlString = textAsHtml),
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewsDetailsScreenPreview() {
    SteamTrackerTheme {
        NewsDetailsScreen(
            news = NewsItem(),
            trackedAppsDetails = listOf()
        )
    }
}

@Composable
fun NewsDetailsErrorScreen(
    retryAction: () -> Unit,
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
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewsDetailsErrorScreenPreview() {
    SteamTrackerTheme {
        AppDetailsErrorScreen({})
    }
}
