package com.example.steamtracker.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.steamtracker.R
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.CollectionApp
import com.example.steamtracker.ui.components.CollectionCard
import com.example.steamtracker.ui.components.CollectionRemoveAlert
import com.example.steamtracker.ui.preview.FakeCollectionsRepository
import com.example.steamtracker.ui.preview.FakeStoreRepository
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@Composable
fun CollectionsScreen(
    collectionsViewModel: CollectionsViewModel,
    collectionsUiState: CollectionsUiState,
    collectionsAppDetails: List<AppDetails?>,
    navigateCollection: () -> Unit,
    onCollectionSelect: (Pair<String, List<CollectionApp>>) -> Unit,
    modifier: Modifier = Modifier
) {
    // State variable for collections dialog windows
    var showCreateDialog by remember { mutableStateOf(false) }

    // State variable for name of new collection
    var collectionName by remember { mutableStateOf("") }

    Scaffold(
        // Only show FAB if the UI state is not Error or Loading
        floatingActionButton = {
            when (collectionsUiState) {
                is CollectionsUiState.Error -> null
                is CollectionsUiState.Loading -> null
                else -> {
                    FloatingActionButton(
                        onClick = {
                            collectionName = ""
                            showCreateDialog = true
                        }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add Collection",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        if (showCreateDialog) {
            CreateCollectionDialog(
                collectionName = collectionName,
                onNameChange = { collectionName = it },
                onDismiss = { showCreateDialog = false },
                onSubmit = {
                    collectionsViewModel.addCollection(it)
                    showCreateDialog = false
                }
            )
        }

        when (collectionsUiState) {
            is CollectionsUiState.Success -> CollectionsMenu(
                collectionsViewModel = collectionsViewModel,
                collections = collectionsUiState.collections,
                collectionsAppDetails = collectionsAppDetails,
                navigateCollection = navigateCollection,
                onCollectionSelect = onCollectionSelect,
                contentPadding = innerPadding
            )
            CollectionsUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
            CollectionsUiState.Error -> CollectionsErrorScreen(
                modifier = modifier.fillMaxSize()
            )
            CollectionsUiState.NoCollections -> Box(
                modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Create a collection to see it displayed here!",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontSize = 18.sp,
                        modifier = modifier.padding(16.dp),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CollectionsScreenPreview() {
    SteamTrackerTheme {
        CollectionsScreen(
            collectionsViewModel = CollectionsViewModel(
                storeRepository = FakeStoreRepository(),
                collectionsRepository = FakeCollectionsRepository(),
                workManager = null
            ),
            collectionsUiState = CollectionsUiState.Success(
                collections = mapOf("collection" to listOf(
                    CollectionApp(
                        collectionName = "name",
                        appId = 0,
                        index = 0
                    )
                ))
            ),
            collectionsAppDetails = listOf(AppDetails()),
            navigateCollection = {},
            onCollectionSelect = {}
        )
    }
}

@Composable
fun CollectionsMenu(
    collectionsViewModel: CollectionsViewModel,
    collections: Map<String, List<CollectionApp>>,
    collectionsAppDetails: List<AppDetails?>,
    navigateCollection: () -> Unit,
    onCollectionSelect: (Pair<String, List<CollectionApp>>) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    var collectionNameToRemove by remember { mutableStateOf<String?>(null) }

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

        LazyColumn(
            modifier = modifier
        ) {
            items(items = collections.entries.toList()) { collection ->
                // Sort collection apps by their index
                val collectionPair = Pair(collection.key, collection.value.sortedBy { it.index })

                if (collectionNameToRemove.equals(collectionPair.first)) {
                    CollectionRemoveAlert(
                        collectionName = collectionPair.first,
                        onDismiss = { collectionNameToRemove = null },
                        onSubmit = {
                            collectionsViewModel.removeCollection(
                                collectionPair.first
                            )
                            collectionNameToRemove = null
                        }
                    )
                }

                CollectionCard(
                    onRemoveClick = { collectionNameToRemove = collectionPair.first },
                    collection = collectionPair,
                    collectionsAppDetails = collectionsAppDetails,
                    navigateCollection = navigateCollection,
                    onCollectionSelect = onCollectionSelect,
                )
            }
        }
    }
}

@Composable
fun CreateCollectionDialog(
    collectionName: String,
    onNameChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Create Collection",
                    style = MaterialTheme.typography.titleMedium
                )

                TextField(
                    value = collectionName,
                    onValueChange = onNameChange,
                    singleLine = true,
                    placeholder = { Text("Collection Name") }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = { onSubmit(collectionName) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Submit")
                    }
                }
            }
        }
    }
}

@Composable
fun CollectionsErrorScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}
