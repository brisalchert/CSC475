package com.example.steamtracker.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.steamtracker.R
import com.example.steamtracker.model.NewsItem
import com.example.steamtracker.ui.components.CollectionsRow
import com.example.steamtracker.ui.components.GeneralInfo
import com.example.steamtracker.ui.components.Genres
import com.example.steamtracker.ui.components.ReviewScore
import com.example.steamtracker.ui.components.ShortInfo
import com.example.steamtracker.ui.components.Tags
import com.example.steamtracker.ui.components.TitleCard
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@Composable
fun NewsDetailsScreen(
    news: NewsItem,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        item {
            TitleCard(appDetails)
        }

        item {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(appDetails.headerImage)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = "Image for ${appDetails.name}",
                contentScale = ContentScale.Crop,
                modifier = modifier.fillMaxWidth()
            )
        }

        item {
            CollectionsRow(
                appDetails = appDetails,
                newsAppsViewModel = newsAppsViewModel
            )
        }

        item {
            GeneralInfo(appDetails, appSpyInfo, modifier)
        }

        item {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                ReviewScore(appSpyInfo, modifier)
            }
        }

        item {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.outlineVariant)
            ) {
                Genres(appDetails, modifier)
            }
        }

        if (appSpyInfo.tags != null) {
            item {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceDim)
                ) {
                    Tags(appSpyInfo, modifier)
                }
            }
        }

        item {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.inversePrimary)
            ) {
                ShortInfo(appDetails, modifier)
            }
        }
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
