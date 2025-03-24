package com.example.steamtracker.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.steamtracker.R
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@Composable
fun Genre(
    genre: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Card(
        modifier = modifier.padding(2.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.genre_container)
        )
    ) {
        Text(
            text = genre,
            modifier = modifier.padding(8.dp),
            color = colorResource(R.color.genre_text)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GenrePreview() {
    SteamTrackerTheme {
        Genre("Genre")
    }
}
