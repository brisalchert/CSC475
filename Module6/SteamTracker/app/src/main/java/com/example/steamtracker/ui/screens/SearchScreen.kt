package com.example.steamtracker.ui.screens

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamtracker.model.SearchAppInfo
import com.example.steamtracker.ui.components.SearchResult
import com.example.steamtracker.ui.components.SearchUiState
import com.example.steamtracker.ui.components.StoreSearchBar
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@Composable
fun SearchScreen(
    searchUiState: SearchUiState,
    getAutocomplete: (query: String) -> Unit,
    clearSearch: () -> Unit,
    autocompleteResults: List<SearchAppInfo>,
    searchResults: List<SearchAppInfo>,
    sortResults: (by: String) -> Unit,
    navigateSearch: () -> Unit,
    onSearch: (query: String) -> Unit,
    navigateApp: () -> Unit,
    onAppSelect: (appId: Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            Spacer(modifier = modifier.height(140.dp))

            when (searchUiState) {
                is SearchUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
                is SearchUiState.NoResults -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceDim
                            )
                        ) {
                            Text(
                                text = "Enter a search query to see results!",
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
                is SearchUiState.Success -> SearchResults(
                    searchResults,
                    sortResults,
                    navigateApp,
                    onAppSelect,
                    modifier,
                    contentPadding
                )
                is SearchUiState.Error -> StoreErrorScreen(
                    retryAction = navigateSearch,
                    modifier = modifier
                )
            }
        }

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
                    text = "Search Results",
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
            }

            StoreSearchBar(
                searchStore = getAutocomplete,
                clearSearch = clearSearch,
                autocompleteResults = autocompleteResults,
                navigateSearch = navigateSearch,
                onSearch = onSearch,
                navigateApp = navigateApp,
                onAppSelect = onAppSelect,
                modifier = modifier.background(MaterialTheme.colorScheme.surfaceDim)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    SteamTrackerTheme {
        SearchScreen(
            searchUiState = SearchUiState.Success(listOf()),
            getAutocomplete = { query -> },
            clearSearch = {},
            autocompleteResults = listOf(),
            searchResults = listOf(),
            sortResults = {},
            navigateSearch = {},
            onSearch = {},
            navigateApp = {},
            onAppSelect = {}
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchResults(
    searchResults: List<SearchAppInfo>,
    sortResults: (String) -> Unit,
    navigateApp: () -> Unit,
    onAppSelect: (appId: Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    if (searchResults.isNotEmpty()) {
        var showMenu by remember { mutableStateOf(false) }
        val context = LocalContext.current

        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(0.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            modifier = Modifier
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onPress = {
                                            showMenu = true
                                        }
                                    )
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.inversePrimary
                            ),
                            elevation = CardDefaults.cardElevation(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Sort,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                contentDescription = "Sort Results",
                                modifier = Modifier.padding(8.dp)
                            )
                        }

                        Text(
                            text = "Sort Results",
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            offset = DpOffset.Zero
                        ) {
                            DropdownMenuItem(
                                text = { Text("Sort by Maximum Price") },
                                onClick = {
                                    sortResults("priceDescending")
                                    Toast.makeText(
                                        context,
                                        "Sorted results by maximum price",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Sort by Minimum Price") },
                                onClick = {
                                    sortResults("priceAscending")
                                    Toast.makeText(
                                        context,
                                        "Sorted results by minimum price",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Sort Alphabetical") },
                                onClick = {
                                    sortResults("nameAscending")
                                    Toast.makeText(
                                        context,
                                        "Sorted results alphabetically",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Sort Reverse Alphabetical") },
                                onClick = {
                                    sortResults("nameDescending")
                                    Toast.makeText(
                                        context,
                                        "Sorted results reverse-alphabetically",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Sort by Maximum Metascore") },
                                onClick = {
                                    sortResults("metascoreDescending")
                                    Toast.makeText(
                                        context,
                                        "Sorted results by maximum metascore",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Sort by Minimum Metascore") },
                                onClick = {
                                    sortResults("metascoreAscending")
                                    Toast.makeText(
                                        context,
                                        "Sorted results by minimum metascore",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    showMenu = false
                                }
                            )
                        }
                    }

                }
            }

            itemsIndexed(searchResults) { index, result ->
                SearchResult(
                    result,
                    navigateApp,
                    onAppSelect,
                    modifier,
                    contentPadding
                )

                if (index < searchResults.lastIndex) { // Avoid adding a divider after the last item
                    Modifier.padding(horizontal = 16.dp)
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    HorizontalDivider(
                        modifier = modifier.padding(horizontal = 10.dp)
                            .background(
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                shape = RoundedCornerShape(4.dp)
                            ),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}
