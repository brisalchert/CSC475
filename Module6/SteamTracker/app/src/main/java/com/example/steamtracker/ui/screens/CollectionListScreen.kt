package com.example.steamtracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.CollectionApp
import com.example.steamtracker.ui.components.AppRemoveAlert
import com.example.steamtracker.ui.components.CollectionAppCard

@Composable
fun CollectionListScreen(
    collectionsViewModel: CollectionsViewModel,
    collection: Pair<String, List<CollectionApp>>,
    collectionAppDetails: List<AppDetails?>,
    navigateApp: () -> Unit,
    onAppSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { TODO() }
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add App",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { innerPadding ->
        var showRemoveDialog by remember { mutableStateOf(false) }

        LazyColumn(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            items(items = collection.second) { app ->
                val appDetails = collectionAppDetails.find {
                    it?.steamAppId == app.appId
                }

                if (appDetails != null) {
                    if (showRemoveDialog) {
                        AppRemoveAlert(
                            appName = appDetails.name,
                            onDismiss = { showRemoveDialog = false },
                            onSubmit = {
                                collectionsViewModel.removeCollectionApp(
                                    collection.first,
                                    appDetails.steamAppId
                                )
                                showRemoveDialog = false
                            }
                        )
                    }

                    CollectionAppCard(
                        onRemoveClick = { showRemoveDialog = true },
                        appDetails = appDetails,
                        navigateApp = navigateApp,
                        onAppSelect = onAppSelect,
                    )
                }
            }
        }
    }
}
