package com.example.steamtracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.steamtracker.model.SearchAppInfo
import com.example.steamtracker.ui.theme.SteamTrackerTheme
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(FlowPreview::class)
@Composable
fun StoreSearchBar(
    searchStore: (query: String) -> Unit,
    clearSearch: () -> Unit,
    autocompleteResults: List<SearchAppInfo>,
    navigateSearch: () -> Unit,
    onSearch: (query: String) -> Unit,
    navigateApp: () -> Unit,
    onAppSelect: (appId: Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Card(
        modifier = modifier
            .wrapContentSize()
            .padding(10.dp)
            .zIndex(1f),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        var query by remember { mutableStateOf("") }
        var isEditing by remember { mutableStateOf(false) }
        val keyboardController = LocalSoftwareKeyboardController.current

        // Debounce autocomplete results, ensuring only necessary updates are processed
        LaunchedEffect(query) {
            snapshotFlow { query }
                .debounce(1000)
                .distinctUntilChanged()
                .collectLatest { newQuery ->
                    // Only update search results when the query changes
                    if (newQuery.isNotEmpty()) {
                        // Store the query before searching to prevent race condition
                        val currentQuery = newQuery
                        searchStore(currentQuery)
                    } else {
                        // Clear the search if there is no query
                        clearSearch()
                    }
                }
        }

        TextField(
            value = query,
            onValueChange = { newQuery ->
                query = newQuery
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    onSearch(query)
                    navigateSearch()
                }
            ),
            placeholder = { Text("Search the Steam Store") },
            singleLine = true,
            leadingIcon = {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = "Menu Icon",
                    modifier = Modifier.padding(PaddingValues(10.dp, 0.dp, 0.dp, 0.dp))
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = {
                        query = ""
                        clearSearch()
                    }) {
                        Icon(Icons.Filled.Close, contentDescription = "Clear Query")
                    }
                } else {
                    Icon(Icons.Filled.Search, contentDescription = null)
                }
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isEditing = focusState.isFocused
                }
        )

        // Only display autocomplete when the user benefits from it
        if (isEditing && autocompleteResults.isNotEmpty()) {
            SearchAutoComplete(
                autocompleteResults,
                navigateApp = navigateApp,
                onAppSelect = onAppSelect
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StoreSearchBarPreview() {
    SteamTrackerTheme {
        StoreSearchBar(
            searchStore = {},
            clearSearch = {},
            autocompleteResults = listOf(),
            navigateSearch = {},
            onSearch = {},
            navigateApp = {},
            onAppSelect = {},
        )
    }
}

@Composable
fun SearchAutoComplete(
    searchResults: List<SearchAppInfo>,
    navigateApp: () -> Unit,
    onAppSelect: (appId: Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier
            .height(300.dp)
            .testTag("AutocompleteList"),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        items(items = searchResults) { item ->
            SearchResult(
                app = item,
                navigateApp = navigateApp,
                onAppSelect = onAppSelect,
                modifier = modifier,
                contentPadding = contentPadding
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchAutoCompletePreview() {
    SteamTrackerTheme {
        SearchAutoComplete(
            searchResults = listOf(SearchAppInfo()),
            navigateApp = {},
            onAppSelect = {}
        )
    }
}
