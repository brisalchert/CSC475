@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.steamtracker.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Feed
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.steamtracker.R
import com.example.steamtracker.ui.screens.StoreScreen
import com.example.steamtracker.ui.screens.TrackerUiState

enum class TrackerScreens {
    Store,
    News,
    Collections,
    Notifications,
    Menu
}

@Composable
fun SteamTrackerApp(
    navController: NavHostController = rememberNavController()
) {
    val focusManager = LocalFocusManager.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route.orEmpty()
    val backStackEntry by navController.currentBackStackEntryAsState()
    var canNavigateBack by remember { mutableStateOf(false) }

    // Dynamically update navigation status based on current navigation destination
    LaunchedEffect(backStackEntry) {
        canNavigateBack = navController.previousBackStackEntry != null
    }

    Scaffold(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures { focusManager.clearFocus() }
            }
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TrackerTopAppBar(
                scrollBehavior = scrollBehavior,
                canNavigateBack = canNavigateBack,
                navigateUp = { navController.navigateUp() }
            )
        },
        bottomBar = {
            TrackerBottomAppBar()
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            NavHost(
                navController = navController,
                startDestination = TrackerScreens.Store.name,
                modifier = Modifier
            ) {
                composable(
                    route = TrackerScreens.Store.name
                ) {
                    StoreScreen(
                        trackerUiState = TrackerUiState.Success,
                        retryAction = {},
                        contentPadding = PaddingValues()
                    )
                }
            }
        }
    }
}

@Composable
fun TrackerTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit
) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun TrackerBottomAppBar(
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        actions = {
            IconButton(
                onClick = {},
                modifier = modifier.weight(1f)
            ) {
                Icon(Icons.AutoMirrored.Filled.Feed, contentDescription = "Featured")
            }
            IconButton(
                onClick = {},
                modifier = modifier.weight(1f)
            ) {
                Icon(Icons.Filled.Newspaper, contentDescription = "News")
            }
            IconButton(
                onClick = {},
                modifier = modifier.weight(1f)
            ) {
                Icon(Icons.Filled.CollectionsBookmark, contentDescription = "Collections")
            }
            IconButton(
                onClick = {},
                modifier = modifier.weight(1f)
            ) {
                Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
            }
            IconButton(
                onClick = {},
                modifier = modifier.weight(1f)
            ) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu")
            }
        },
        modifier = modifier
    )
}
