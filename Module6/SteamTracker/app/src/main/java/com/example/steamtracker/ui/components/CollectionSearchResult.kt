package com.example.steamtracker.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.steamtracker.R
import com.example.steamtracker.model.CollectionApp
import com.example.steamtracker.model.Platforms
import com.example.steamtracker.model.SearchAppInfo
import com.example.steamtracker.ui.preview.FakeCollectionsRepository
import com.example.steamtracker.ui.preview.FakeStoreRepository
import com.example.steamtracker.ui.screens.CollectionsViewModel
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@Composable
fun CollectionSearchResult(
    collectionsViewModel: CollectionsViewModel,
    currentCollection: Pair<String, List<CollectionApp>>,
    onAddApp: (collectionName: String, appId: Int) -> Unit,
    onRemoveApp: (collectionName: String, appId: Int) -> Unit,
    app: SearchAppInfo,
    navigateApp: () -> Unit,
    onAppSelect: (appId: Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val onList by collectionsViewModel.isInCollection(currentCollection.first, app.id).collectAsState(
        initial = false
    )

    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        onClick = {
            onAppSelect(app.id)
            navigateApp()
        }
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
                    .height(64.dp)
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
                IconButton(
                    onClick = {
                        // Check if app is currently in the collection
                        if (onList) {
                            onRemoveApp(currentCollection.first, app.id)

                            Toast.makeText(
                                context,
                                "App removed from collection ${currentCollection.first}",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            onAddApp(currentCollection.first, app.id)

                            Toast.makeText(
                                context,
                                "App added to collection ${currentCollection.first}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    // Check if app is currently in the collection
                    if (onList) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "Remove from Collection"
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Add,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "Add to Collection"
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CollectionSearchResultPreview() {
    SteamTrackerTheme {
        CollectionSearchResult(
            collectionsViewModel = CollectionsViewModel(
                storeRepository = FakeStoreRepository(),
                collectionsRepository = FakeCollectionsRepository(),
                workManager = null
            ),
            currentCollection = Pair("collection", listOf()),
            onAddApp = { string, int -> },
            onRemoveApp = { string, int -> },
            app = SearchAppInfo(),
            navigateApp = {},
            onAppSelect = {}
        )
    }
}
