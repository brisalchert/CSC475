package com.example.steamtracker.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.steamtracker.R
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.Screenshot
import com.example.steamtracker.model.SteamSpyAppRequest
import com.example.steamtracker.ui.components.AppPage
import com.example.steamtracker.ui.components.NewsAppsViewModel
import com.example.steamtracker.ui.components.PreferencesViewModel
import com.example.steamtracker.ui.preview.FakeCollectionsRepository
import com.example.steamtracker.ui.preview.FakePreferencesRepository
import com.example.steamtracker.ui.preview.FakeSteamworksRepository
import com.example.steamtracker.ui.preview.FakeStoreRepository
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@Composable
fun AppDetailsScreen(
    appDetailsUiState: AppDetailsUiState,
    getAppDetails: (appId: Int) -> Unit,
    newsAppsViewModel: NewsAppsViewModel,
    collectionsViewModel: CollectionsViewModel,
    preferencesViewModel: PreferencesViewModel,
    navigateScreenshot: () -> Unit,
    onScreenshotSelect: (screenshot: Screenshot) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    when(appDetailsUiState) {
        is AppDetailsUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is AppDetailsUiState.Success -> {
            if (appDetailsUiState.appDetails == null) {
                AppDetailsErrorScreen(
                    retryAction = { getAppDetails(appDetailsUiState.appId) }
                )
            } else {
                AppPage(
                    appDetails = appDetailsUiState.appDetails,
                    appSpyInfo = appDetailsUiState.appSpyInfo,
                    newsAppsViewModel = newsAppsViewModel,
                    collectionsViewModel = collectionsViewModel,
                    preferencesViewModel = preferencesViewModel,
                    navigateScreenshot = navigateScreenshot,
                    onScreenshotSelect = onScreenshotSelect,
                    modifier = modifier,
                    contentPadding = contentPadding
                )
            }
        }
        is AppDetailsUiState.Error -> AppDetailsErrorScreen(
            retryAction = { getAppDetails(appDetailsUiState.appId) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppDetailsScreenPreview() {
    SteamTrackerTheme { 
        AppDetailsScreen(
            appDetailsUiState = AppDetailsUiState.Success(
                appDetails = AppDetails(),
                appSpyInfo = SteamSpyAppRequest(),
                appId = 0
            ),
            getAppDetails = {},
            newsAppsViewModel = NewsAppsViewModel(
                steamworksRepository = FakeSteamworksRepository(),
                storeRepository = FakeStoreRepository()
            ),
            collectionsViewModel = CollectionsViewModel(
                storeRepository = FakeStoreRepository(),
                collectionsRepository = FakeCollectionsRepository(),
                workManager = null
            ),
            preferencesViewModel = PreferencesViewModel(
                preferencesRepository = FakePreferencesRepository()
            ),
            navigateScreenshot = {},
            onScreenshotSelect = {}
        )
    }
}

@Composable
fun AppDetailsErrorScreen(
    retryAction: () -> Unit,
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
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppDetailsErrorScreenPreview() {
    SteamTrackerTheme {
        AppDetailsErrorScreen({})
    }
}
