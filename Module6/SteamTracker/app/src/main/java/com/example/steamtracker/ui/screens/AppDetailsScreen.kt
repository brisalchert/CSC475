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
import androidx.lifecycle.ViewModel
import com.example.steamtracker.R
import com.example.steamtracker.ui.components.AppPage
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@Composable
fun AppDetailsScreen(
    appDetailsUiState: AppDetailsUiState,
    getAppDetails: (appId: Int) -> Unit,
    newsAppsViewModel: ViewModel,
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
