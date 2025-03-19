package com.example.steamtracker.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.steamtracker.model.Screenshot
import com.example.steamtracker.ui.components.FeaturedTab
import com.example.steamtracker.ui.components.StoreSearchBar
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@Composable
fun StoreScreen(
    trackerUiState: TrackerUiState,
    retryAction: () -> Unit, // Retry function for the error screen
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    when (trackerUiState) {
        is TrackerUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is TrackerUiState.Success -> StoreScreenContainer(
            trackerUiState.photos,
            modifier = modifier.fillMaxWidth(),
            contentPadding = contentPadding
        )
        is TrackerUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Preview(showBackground = true)
@Composable
fun StoreScreenPreview() {
    SteamTrackerTheme {
        StoreScreen(TrackerUiState.Success(listOf()), {})
    }
}

@Composable
fun StoreScreenContainer(
    photos: List<Pair<String, List<Screenshot>>>,
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
                selectedTabIndex = tabIndex
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                }
            }

            when(tabIndex) {
                // TODO: Implement screens for each tab
                0 -> FeaturedTab(photos, modifier, contentPadding)
            }
        }
    }
}
