package com.example.steamtracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamtracker.model.NewsItem
import com.example.steamtracker.utils.formatUnixTimestampSeconds

@Composable
fun NewsCard(
    newsItem: NewsItem,
    appName: String,
    modifier: Modifier = Modifier
) {
    Card( // TODO: Implement clicking on news card to go to details
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = modifier
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Display the game this post is for
            Text(
                text = appName,
                fontSize = 18.sp
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
