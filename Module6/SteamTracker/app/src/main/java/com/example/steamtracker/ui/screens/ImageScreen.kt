package com.example.steamtracker.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.steamtracker.R
import com.example.steamtracker.model.Screenshot
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@Composable
fun ImageScreen(
    screenshot: Screenshot,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = modifier.wrapContentSize(),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(screenshot.pathFull)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = "Fullscreen screenshot"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImageScreenPreview() {
    SteamTrackerTheme {
        ImageScreen(
            screenshot = Screenshot()
        )
    }
}
