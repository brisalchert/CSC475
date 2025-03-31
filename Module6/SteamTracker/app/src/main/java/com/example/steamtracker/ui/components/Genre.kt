package com.example.steamtracker.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.steamtracker.R
import com.example.steamtracker.data.PreferencesRepository
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@Composable
fun Genre(
    genre: String,
    preferencesViewModel: PreferencesViewModel,
    modifier: Modifier = Modifier
) {
    val favoriteGenres by preferencesViewModel.favoriteGenres.collectAsState()

    val context = LocalContext.current

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.genre_container)
        ),
        onClick = {
            if (favoriteGenres.contains(genre)) {
                preferencesViewModel.removeFavoriteGenre(genre)
                Toast.makeText(
                    context,
                    "Removed \"$genre\" from favorite genres",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                preferencesViewModel.addFavoriteGenre(genre)
                Toast.makeText(
                    context,
                    "Added \"$genre\" to favorite genres",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageVector: ImageVector = if (favoriteGenres.contains(genre)) {
                Icons.Default.Check
            } else {
                Icons.Default.Add
            }

            Icon(
                imageVector = imageVector,
                contentDescription = "Check Icon",
                tint = colorResource(R.color.genre_text)
            )

            Text(
                text = genre,
                color = colorResource(R.color.genre_text)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GenrePreview() {
    SteamTrackerTheme {
        Genre("Genre", PreferencesViewModel(PreferencesRepository(LocalContext.current)))
    }
}
