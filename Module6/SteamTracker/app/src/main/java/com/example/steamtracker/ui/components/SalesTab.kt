package com.example.steamtracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.SteamSpyAppRequest
import com.example.steamtracker.ui.screens.LoadingScreen
import com.example.steamtracker.ui.screens.StoreErrorScreen
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@Composable
fun SalesTab(
    salesUiState: SalesUiState,
    getSales: () -> Unit,
    salesAppDetails: List<AppDetails?>,
    navigateApp: () -> Unit,
    onAppSelect: (appId: Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    when (salesUiState) {
        is SalesUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is SalesUiState.Success -> SalesGamesList(
            salesGames = salesUiState.salesGames
                .sortedByDescending { it.discount },
            salesAppDetails = salesAppDetails,
            navigateApp = navigateApp,
            onAppSelect = onAppSelect,
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
        SalesTab(
            salesUiState = SalesUiState.Success(listOf()),
            getSales = {},
            salesAppDetails = listOf(),
            navigateApp = {},
            onAppSelect = {}
        )
    }
}

@Composable
fun SalesGamesList(
    salesGames: List<SteamSpyAppRequest>,
    salesAppDetails: List<AppDetails?>,
    navigateApp: () -> Unit,
    onAppSelect: (appId: Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Connect salesGames to appDetails for images
        val appDetailsMap = salesAppDetails.associateBy { it?.steamAppId ?: 0 }
        val salesDetailsPairs = salesGames.map { sales ->
            val appDetails = appDetailsMap[sales.appid]
            sales to appDetails
        }

        items(items = salesDetailsPairs) { details ->
            SalesApp(
                appInfo = details.first,
                appDetails = details.second,
                navigateApp = navigateApp,
                onAppSelect = onAppSelect,
                modifier = modifier
            )
        }
    }
}
