package com.example.steamtracker.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamtracker.R
import com.example.steamtracker.data.PreferencesRepository
import com.example.steamtracker.ui.components.PreferencesViewModel
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PreferencesScreen(
    preferencesViewModel: PreferencesViewModel,
    modifier: Modifier = Modifier
) {
    val favoriteGenres by preferencesViewModel.favoriteGenres.collectAsState()
    val favoriteTags by preferencesViewModel.favoriteTags.collectAsState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .shadow(8.dp, RoundedCornerShape(0.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "PREFERENCES",
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
        }

        Card(
            modifier = modifier
                .fillMaxSize()
                .padding(12.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceDim
            )
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.Start
                ) {
                    stickyHeader {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                                .shadow(8.dp, RoundedCornerShape(0.dp))
                                .background(MaterialTheme.colorScheme.outlineVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Favorite Genres",
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    if (favoriteGenres.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Card(
                                    elevation = CardDefaults.cardElevation(8.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                ) {
                                    Text(
                                        text = "Add genres to see them here!",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    } else {
                        items(items = favoriteGenres.toList().sorted()) {genre ->
                            PreferencesGenre(
                                preferencesViewModel = preferencesViewModel,
                                genre = genre,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                            )
                        }
                    }

                    stickyHeader {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                                .shadow(8.dp, RoundedCornerShape(0.dp))
                                .background(MaterialTheme.colorScheme.outlineVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Favorite Tags",
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    if (favoriteTags.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Card(
                                    elevation = CardDefaults.cardElevation(8.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                ) {
                                    Text(
                                        text = "Add tags to see them here!",
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        )
                                    )
                                }
                            }
                        }
                    } else {
                        items(items = favoriteTags.toList().sorted()) { tag ->
                            PreferencesTag(
                                preferencesViewModel = preferencesViewModel,
                                tag = tag,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreferencesScreenPreview() {
    SteamTrackerTheme {
        PreferencesScreen(
            preferencesViewModel = PreferencesViewModel(
                PreferencesRepository(LocalContext.current)
            )
        )
    }
}

@Composable
fun PreferencesGenre(
    preferencesViewModel: PreferencesViewModel,
    genre: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.genre_container)
        )
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = genre,
                color = colorResource(R.color.genre_text)
            )

            Icon(
                imageVector = Icons.Default.RemoveCircle,
                contentDescription = "Remove genre",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.clickable {
                    preferencesViewModel.removeFavoriteGenre(genre)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreferencesGenrePreview() {
    SteamTrackerTheme {
        PreferencesGenre(
            preferencesViewModel = PreferencesViewModel(
                PreferencesRepository(LocalContext.current)
            ),
            genre = "Genre"
        )
    }
}

@Composable
fun PreferencesTag(
    preferencesViewModel: PreferencesViewModel,
    tag: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.tag_container)
        )
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = tag,
                color = colorResource(R.color.tag_text)
            )

            Icon(
                imageVector = Icons.Default.RemoveCircle,
                contentDescription = "Remove tag",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.clickable {
                    preferencesViewModel.removeFavoriteTag(tag)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreferencesTagPreview() {
    SteamTrackerTheme {
        PreferencesTag(
            preferencesViewModel = PreferencesViewModel(
                PreferencesRepository(LocalContext.current)
            ),
            tag = "Tag"
        )
    }
}
