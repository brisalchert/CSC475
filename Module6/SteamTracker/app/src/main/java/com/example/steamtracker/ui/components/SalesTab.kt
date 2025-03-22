package com.example.steamtracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.steamtracker.model.SteamSpyAppRequest
import com.example.steamtracker.ui.screens.LoadingScreen
import com.example.steamtracker.ui.screens.StoreErrorScreen
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@Composable
fun SalesTab(
    salesUiState: SalesUiState,
    getSales: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    when (salesUiState) {
        is SalesUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is SalesUiState.Success -> SalesGamesList(
            salesGames = salesUiState.salesGames,
            modifier = modifier,
            contentPadding = contentPadding
        )
        is SalesUiState.Error -> StoreErrorScreen(
            retryAction = getSales,
            modifier = modifier.fillMaxSize()
        )
    }


}

@Preview(showBackground = true)
@Composable
fun SalesTabPreview() {
    SteamTrackerTheme {
        SalesTab(SalesUiState.Success(listOf()), {})
    }
}

@Composable
fun SalesGamesList(
    salesGames: List<SteamSpyAppRequest>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        items(items = salesGames) { game ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(game.name)
                Text(game.discount)
            }
        }
    }
}
