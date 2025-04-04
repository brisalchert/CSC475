package com.example.steamtracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.CollectionApp
import com.example.steamtracker.ui.components.AppRemoveAlert
import com.example.steamtracker.ui.components.CollectionAppCard
import com.example.steamtracker.ui.preview.FakeCollectionsRepository
import com.example.steamtracker.ui.preview.FakeStoreRepository
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@Composable
fun CollectionListScreen(
    collectionsViewModel: CollectionsViewModel,
    collection: Pair<String, List<CollectionApp>>,
    collectionAppDetails: List<AppDetails?>,
    navigateApp: () -> Unit,
    onAppSelect: (Int) -> Unit,
    navigateAddApp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentCollection by collectionsViewModel.getCollectionContents(collection.first).collectAsState(
        initial = emptyList()
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateAddApp
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add App",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { innerPadding ->
        var appIdToRemove by remember { mutableStateOf<Int?>(null) }

        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .shadow(8.dp, RoundedCornerShape(0.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "COLLECTIONS",
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .shadow(8.dp, RoundedCornerShape(0.dp))
                    .background(MaterialTheme.colorScheme.outlineVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = collection.first.uppercase(),
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
            }

            if (currentCollection.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .background(
                            MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Add games to the collection to see them displayed here!",
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top
                ) {
                    items(items = currentCollection, key = { it.appId }) { app ->
                        val appDetails = collectionAppDetails.find {
                            it?.steamAppId == app.appId
                        }

                        if (appDetails != null) {
                            if (appIdToRemove == app.appId) {
                                AppRemoveAlert(
                                    appName = appDetails.name,
                                    onDismiss = { appIdToRemove = null },
                                    onSubmit = {
                                        collectionsViewModel.removeCollectionApp(
                                            collection.first,
                                            appDetails.steamAppId
                                        )
                                        appIdToRemove = null
                                    }
                                )
                            }

                            CollectionAppCard(
                                onRemoveClick = { appIdToRemove = app.appId },
                                appDetails = appDetails,
                                navigateApp = navigateApp,
                                onAppSelect = onAppSelect,
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
fun CollectionListScreenPreview() {
    SteamTrackerTheme {
        CollectionListScreen(
            collectionsViewModel = CollectionsViewModel(
                storeRepository = FakeStoreRepository(),
                collectionsRepository = FakeCollectionsRepository(),
                workManager = null
            ),
            collection = Pair("collection", listOf()),
            collectionAppDetails = listOf(AppDetails()),
            navigateApp = {},
            onAppSelect = {},
            navigateAddApp = {}
        )
    }
}
