package com.example.steamtracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.steamtracker.R
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.NewsItem
import com.example.steamtracker.utils.formatUnixTimestampSeconds

@Composable
fun NewsCard(
    newsItem: NewsItem,
    appDetails: AppDetails?,
    modifier: Modifier = Modifier
) {
    Card( // TODO: Implement clicking on news card to go to details
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
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

            Column(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
            ) {
                // Display the game this post is for
                Text(
                    text = appDetails?.name ?: "Name Unavailable",
                    fontSize = 18.sp,
                )

                // Display the date of the post
                Text(
                    text = formatUnixTimestampSeconds(newsItem.date),
                    fontSize = 16.sp
                )

                // Display title of news post
                Text(
                    text = newsItem.title,
                    fontSize = 24.sp
                )
            }
        }
    }
}
