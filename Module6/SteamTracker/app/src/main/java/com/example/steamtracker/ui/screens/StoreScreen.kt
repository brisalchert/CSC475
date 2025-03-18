package com.example.steamtracker.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.preferKeepClear
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.steamtracker.R
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
        is TrackerUiState.Success -> FeaturedGameList(
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
        StoreScreen(TrackerUiState.Success, {})
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    SteamTrackerTheme {
        LoadingScreen()
    }
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
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
fun ErrorScreenPreview() {
    SteamTrackerTheme {
        ErrorScreen({})
    }
}

@Composable
fun FeaturedGameList(
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
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeaturedGameListPreview() {
    SteamTrackerTheme {
        FeaturedGameList()
    }
}

@Composable
fun StoreSearchBar(
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
        val query = remember { mutableStateOf("") }

        TextField(
            value = query.value,
            onValueChange = { query.value = it },
            placeholder = { Text("Search the Steam Store") },
            singleLine = true,
            leadingIcon = {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = "Menu Icon",
                    modifier = modifier.padding(PaddingValues(10.dp, 0.dp, 0.dp, 0.dp))
                )
            },
            trailingIcon = {
                if (query.value.isNotEmpty()) {
                    IconButton(onClick = { query.value = "" }) {
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
        )

        // TODO: Add list of autocomplete results
        if (query.value.isNotEmpty()) {
            Column(modifier = modifier.height(200.dp)) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StoreSearchBarPreview() {
    SteamTrackerTheme {
        StoreSearchBar()
    }
}
