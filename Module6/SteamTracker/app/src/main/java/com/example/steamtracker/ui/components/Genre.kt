package com.example.steamtracker.ui.components

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.steamtracker.R
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@Composable
fun Genre(
    genre: String,
    favorite: Boolean,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        modifier = modifier
            .padding(2.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        showMenu = true
                    }
                )
            },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.genre_container)
        )
    ) {
        Row(
            modifier = modifier.padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (favorite) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check Icon",
                    tint = colorResource(R.color.genre_text)
                )
            }

            Text(
                text = genre,
                color = colorResource(R.color.genre_text)
            )

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                offset = DpOffset.Zero
            ) {
                val text: String = if (favorite) {
                    "Remove genre from favorites"
                } else {
                    "Add genre to favorites"
                }

                DropdownMenuItem(
                    text = { Text(text) },
                    onClick = {
                        val text: String = if (favorite) {
                            "Genre \"$genre\" removed from favorites"
                        } else {
                            "Genre \"$genre\" added to favorites"
                        }

                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                        showMenu = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GenrePreview() {
    SteamTrackerTheme {
        Genre("Genre", true)
    }
}
