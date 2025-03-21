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
import com.example.steamtracker.ui.components.FeaturedTab
import com.example.steamtracker.ui.components.StoreSearchBar
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@Composable
fun StoreScreen(
    storeUiState: StoreUiState,
    getFeatured: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Featured", "On Sale", "Recommended")

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        StoreSearchBar()

        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = modifier.height(76.dp))

            TabRow(
                selectedTabIndex = tabIndex,
                modifier = modifier
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                }
            }

            when (storeUiState) {
                is StoreUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
                is StoreUiState.SuccessFeatured -> FeaturedTab(
                    storeUiState.featuredGames,
                    modifier = modifier,
                    contentPadding = contentPadding
                )
                is StoreUiState.Error -> StoreErrorScreen(
                    when(tabIndex) {
                        0 -> getFeatured
                        1 -> getFeatured
                        2 -> getFeatured
                        else -> ({})
                    },
                    modifier = modifier.fillMaxSize()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StoreScreenPreview() {
    SteamTrackerTheme {
        StoreScreen(
            StoreUiState.SuccessFeatured(listOf()),
            {}
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
