package com.example.steamtracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.steamtracker.R
import com.example.steamtracker.model.FeaturedGame
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@Composable
fun FeaturedTab(
    featuredGames: List<FeaturedGame>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items = featuredGames) { game ->
            PhotosGrid(
                game = game.name,
                photoPaths = listOf(
                    game.headerImage,
                    game.largeCapsuleImage,
                    game.smallCapsuleImage
                ),
                modifier = modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeaturedTabPreview() {
    SteamTrackerTheme {
        FeaturedTab(listOf(
            FeaturedGame(
                id = 0,
                type = 0,
                name = "Game Name",
                discounted = false,
                discountPercent = 0,
                originalPrice = 5999,
                finalPrice = 5999,
                currency = "USD",
                largeCapsuleImage = "pathLarge",
                smallCapsuleImage = "pathSmall",
                windowsAvailable = true,
                macAvailable = true,
                linuxAvailable = true,
                streamingVideoAvailable = true,
                headerImage = "pathHeader"
            )
        ))
    }
}

/**
 * Card for a single screenshot in the gallery
 */
@Composable
fun GalleryPhotoCard(
    path: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(path)
                .crossfade(true)
                .build(),
            error = painterResource(R.drawable.ic_broken_image),
            placeholder = painterResource(R.drawable.loading_img),
            contentDescription = stringResource(R.string.photo),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Grid of photos for a particular game
 */
@Composable
fun PhotosGrid(
    game: String,
    photoPaths: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (photoPaths.isEmpty()) {
            Text(
                text = "Game not found", // For invalid game ID values
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
        } else {
            Text(
                text = game,
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                photoPaths.chunked(3).forEach { photoRow ->
                    Row(
                        modifier = modifier
                    ) {
                        photoRow.forEach { photo ->
                            GalleryPhotoCard(
                                photo,
                                modifier = modifier
                                    .padding(4.dp)
                                    .weight(1f)
                                    .aspectRatio(1.5f)
                            )
                        }

                        // Fill last row with empty space if fewer than 3 images
                        repeat(3 - photoRow.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
