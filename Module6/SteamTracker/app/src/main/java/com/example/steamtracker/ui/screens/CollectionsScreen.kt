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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.steamtracker.R
import com.example.steamtracker.model.AppDetails

@Composable
fun CollectionsScreen(
    collectionsViewModel: CollectionsViewModel,
    collectionsUiState: CollectionsUiState,
    collectionsAppDetails: List<AppDetails?>,
    navigateApp: () -> Unit,
    onAppSelect: (appId: Int) -> Unit,
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
                        onClick = { showCreateDialog = true }
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
                }
            )
        }

        when (collectionsUiState) {
            is CollectionsUiState.Success -> CollectionsMenu(
                collectionsViewModel = collectionsViewModel,
                collectionsUiState = collectionsUiState,
                collectionsAppDetails = collectionsAppDetails,
                navigateApp = navigateApp,
                onAppSelect = onAppSelect,
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

@Composable
fun CollectionsMenu(
    collectionsViewModel: CollectionsViewModel,
    collectionsUiState: CollectionsUiState,
    collectionsAppDetails: List<AppDetails?>,
    navigateApp: () -> Unit,
    onAppSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {

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
                    placeholder = { Text("Collection Name") }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    TextButton(onClick = { onSubmit(collectionName) }) {
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
