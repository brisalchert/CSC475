package com.example.steamtracker.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.steamtracker.ui.components.FeaturedTab
import com.example.steamtracker.ui.components.StoreSearchBar
import com.example.steamtracker.ui.theme.SteamTrackerTheme
import kotlin.math.exp

@Composable
fun StoreScreen(
    trackerUiState: TrackerUiState,
    getFeatured: () -> Unit, // Retry function for the error screen
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

            when (trackerUiState) {
                is TrackerUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
                is TrackerUiState.SuccessPhotos -> ErrorScreen(({}), modifier = modifier.fillMaxSize())
                is TrackerUiState.SuccessFeatured -> FeaturedTab(
                    trackerUiState.featuredGames,
                    modifier = modifier,
                    contentPadding = contentPadding
                )
                is TrackerUiState.Error -> ErrorScreen(
                    when(tabIndex) {
                        0 -> getFeatured
                        1 -> getFeatured
                        2 -> getFeatured
                        else -> ({})
                    },
                    modifier = modifier.fillMaxSize()
                )
            }

            when(tabIndex) {
                // TODO: Implement screens for each tab
                0 -> getFeatured()
                1 -> getFeatured()
                2 -> getFeatured()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StoreScreenPreview() {
    SteamTrackerTheme {
        StoreScreen(TrackerUiState.SuccessPhotos(listOf()), {})
    }
}
