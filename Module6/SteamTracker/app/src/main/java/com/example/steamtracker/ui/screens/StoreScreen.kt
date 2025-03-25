package com.example.steamtracker.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.steamtracker.R
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.FeaturedCategoriesRequest
import com.example.steamtracker.model.SearchAppInfo
import com.example.steamtracker.ui.components.FeaturedTab
import com.example.steamtracker.ui.components.FeaturedUiState
import com.example.steamtracker.ui.components.SalesTab
import com.example.steamtracker.ui.components.SalesUiState
import com.example.steamtracker.ui.components.StoreSearchBar
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@Composable
fun StoreScreen(
    tabIndex: Int,
    onTabChange: (Int) -> Unit,
    featuredUiState: FeaturedUiState,
    getFeatured: () -> Unit,
    salesUiState: SalesUiState,
    getSales: () -> Unit,
    salesAppDetails: List<AppDetails?>,
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
    var storeTabIndex by remember { mutableIntStateOf(tabIndex) }
    val tabs = listOf("Featured", "On Sale", "Recommended")

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        StoreSearchBar(
            searchStore = searchStore,
            clearSearch = clearSearch,
            autocompleteResults = autocompleteResults,
            navigateSearch = navigateSearch,
            onSearch = onSearch,
            navigateApp = navigateApp,
            onAppSelect = onAppSelect
        )

        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = modifier.height(76.dp))

            TabRow(
                selectedTabIndex = storeTabIndex,
                modifier = modifier
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = storeTabIndex == index,
                        onClick = {
                            storeTabIndex = index
                            onTabChange(index)
                        }
                    )
                }
            }

            // Display the correct screen for the current selected tab
            when (storeTabIndex) {
                0 -> FeaturedTab(
                    featuredUiState = featuredUiState,
                    getFeatured = getFeatured,
                    navigateApp = navigateApp,
                    onAppSelect = onAppSelect,
                    modifier = modifier,
                    contentPadding = contentPadding
                )
                1 -> SalesTab(
                    salesUiState = salesUiState,
                    getSales = getSales,
                    salesAppDetails = salesAppDetails,
                    navigateApp = navigateApp,
                    onAppSelect = onAppSelect,
                    modifier = modifier,
                    contentPadding = contentPadding
                )
                2 -> Column {} // TODO: Implement recommendations
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StoreScreenPreview() {
    SteamTrackerTheme {
        StoreScreen(
            tabIndex = 0,
            onTabChange = {},
            featuredUiState = FeaturedUiState.Success(
                FeaturedCategoriesRequest()
            ),
            getFeatured = {},
            salesUiState = SalesUiState.Success(listOf()),
            getSales = {},
            salesAppDetails = listOf(),
            searchStore = { string: String -> },
            clearSearch = {},
            autocompleteResults = listOf(),
            navigateSearch = {},
            onSearch = {},
            navigateApp = {},
            onAppSelect = {}
        )
    }
}

@Composable
fun StoreErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StoreErrorScreenPreview() {
    SteamTrackerTheme {
        StoreErrorScreen({})
    }
}
