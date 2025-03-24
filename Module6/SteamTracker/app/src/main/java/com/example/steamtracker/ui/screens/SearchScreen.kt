package com.example.steamtracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
    searchStore: (query: String) -> Unit,
    clearSearch: () -> Unit,
    autocompleteResults: List<SearchAppInfo>,
    searchResults: List<SearchAppInfo>,
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
                is SearchUiState.Success -> SearchResults(
                    searchResults,
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
                searchStore = searchStore,
                clearSearch = clearSearch,
                autocompleteResults = autocompleteResults,
                navigateSearch = navigateSearch,
                onSearch = onSearch,
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
            searchStore = { query -> },
            clearSearch = {},
            autocompleteResults = listOf(),
            searchResults = listOf(),
            navigateSearch = {},
            onSearch = {},
            navigateApp = {},
            onAppSelect = {}
        )
    }
}

@Composable
fun SearchResults(
    searchResults: List<SearchAppInfo>,
    navigateApp: () -> Unit,
    onAppSelect: (appId: Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    if (searchResults.isNotEmpty()) {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
