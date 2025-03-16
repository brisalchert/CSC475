package com.example.photogallery.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
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
import com.example.photogallery.R
import com.example.photogallery.model.Screenshot
import com.example.photogallery.ui.theme.PhotoGalleryTheme

@Composable
fun HomeScreen(
    galleryUiState: GalleryUiState,
    retryAction: () -> Unit,
    onImageClicked: (imageUri: String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    when (galleryUiState) {
        is GalleryUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is GalleryUiState.Success -> GameList(
            galleryUiState.photos,
            onImageClicked = onImageClicked,
            contentPadding = contentPadding,
            modifier = modifier.fillMaxWidth()
        )

        is GalleryUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

/**
 * ResultScreen displaying number of photos retrieved.
 */
@Composable
fun ResultScreen(photos: String, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(text = photos)
    }
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
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
fun ErrorScreenPreview() {
    PhotoGalleryTheme {
        ErrorScreen({})
    }
}

@Preview(showBackground = true)
@Composable
fun PhotosGridScreenPreview() {
    PhotoGalleryTheme {
        val mockGame = ""
        val mockPhotos = List(10) { Screenshot(it, "", "") }
        PhotosGrid(mockGame, mockPhotos, {})
    }
}

@Composable
fun GalleryPhotoCard(
    photo: Screenshot,
    onImageClicked: (imageUri: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable {
                onImageClicked(photo.pathFull)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(photo.pathThumbnail)
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

@Composable
fun PhotosGrid(
    game: String,
    photos: List<Screenshot>,
    onImageClicked: (imageUri: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (photos.isEmpty()) {
            Text(
                text = "Game not found",
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
                photos.chunked(3).forEach { photoRow ->
                    Row(
                        modifier = modifier
                    ) {
                        photoRow.forEach { photo ->
                            GalleryPhotoCard(
                                photo,
                                onImageClicked = onImageClicked,
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

@Composable
fun GameList(
    gameList: List<Pair<String, List<Screenshot>>>,
    onImageClicked: (imageUri: String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items = gameList) { (game, photos) ->
            PhotosGrid(
                game = game,
                photos = photos,
                onImageClicked = onImageClicked,
                modifier = modifier
            )
        }
    }
}
