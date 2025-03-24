package com.example.steamtracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.steamtracker.R
import com.example.steamtracker.model.Platforms
import com.example.steamtracker.model.SearchAppInfo
import com.example.steamtracker.ui.theme.SteamTrackerTheme
import com.example.steamtracker.utils.formatCurrency

@Composable
fun SearchResult(
    app: SearchAppInfo,
    newsAppsViewModel: ViewModel,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(app.tinyImage)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = "Image for ${app.name}",
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .width(128.dp)
                    .height(48.dp)
            )

            Text(
                text = app.name,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )

            Spacer(modifier = Modifier.width(32.dp))

            Box(
                modifier = modifier.padding(end = 8.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (app.price != null) {
                    // Show app price only if it is provided
                    if (app.price.initial != app.price.final) {
                        // Show both the original and discounted prices
                        Column {
                            Text(
                                text = formatCurrency(app.price.initial.div(100.0)),
                                fontSize = 16.sp,
                                textDecoration = TextDecoration.LineThrough,
                                color = MaterialTheme.colorScheme.outline
                            )

                            Text(
                                text = formatCurrency(app.price.final.div(100.0)),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        Text(
                            text = formatCurrency(app.price.final.div(100.0)),
                            fontSize = 16.sp,
                        )
                    }
                } else {
                    // If not provided, the app is free to play
                    Text(
                        text = "FREE",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchResultPreview() {
    SteamTrackerTheme {
        SearchResult(
            app = SearchAppInfo(
                type = "App",
                name = "Game",
                id = 0,
                price = null,
                tinyImage = "url",
                metascore = "90",
                platforms = Platforms(
                    true,
                    false,
                    false
                ),
                streamingvideo = false,
            ),
            newsAppsViewModel = object: ViewModel() {}
        )
    }
}
