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
import com.example.steamtracker.data.NetworkPreferencesRepository
import com.example.steamtracker.data.dataStore
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@Composable
fun Tag(
    tag: String,
    preferencesViewModel: PreferencesViewModel,
    modifier: Modifier = Modifier
) {
    val favoriteTags by preferencesViewModel.favoriteTags.collectAsState()

    val context = LocalContext.current

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.tag_container)
        ),
        onClick = {
            if (favoriteTags.contains(tag)) {
                preferencesViewModel.removeFavoriteTag(tag)
                Toast.makeText(
                    context,
                    "Removed \"$tag\" from favorite tags",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                preferencesViewModel.addFavoriteTag(tag)
                Toast.makeText(
                    context,
                    "Added \"$tag\" to favorite tags",
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
            val imageVector: ImageVector = if (favoriteTags.contains(tag)) {
                Icons.Default.Check
            } else {
                Icons.Default.Add
            }

            val contentDescription = if (favoriteTags.contains(tag)) {
                "Tag Added to Favorites"
            } else {
                "Tag not on Favorites"
            }

            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint = colorResource(R.color.tag_text)
            )

            Text(
                text = tag,
                color = colorResource(R.color.tag_text),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TagPreview() {
    SteamTrackerTheme {
        Tag("Tag", PreferencesViewModel(NetworkPreferencesRepository(LocalContext.current.dataStore)))
    }
}
