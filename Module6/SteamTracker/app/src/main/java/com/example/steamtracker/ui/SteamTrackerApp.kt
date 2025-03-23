@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.steamtracker.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.steamtracker.R
import com.example.steamtracker.ui.screens.AppDetailsScreen
import com.example.steamtracker.ui.screens.AppDetailsViewModel
import com.example.steamtracker.ui.screens.StoreScreen
import com.example.steamtracker.ui.components.FeaturedViewModel
import com.example.steamtracker.ui.components.SalesViewModel
import com.example.steamtracker.ui.components.SearchViewModel
import com.example.steamtracker.ui.screens.NewsScreen
import com.example.steamtracker.ui.screens.NewsViewModel

enum class TrackerScreens {
    Store,
    News,
    Collections,
    Notifications,
    Menu
}

@Composable
fun SteamTrackerApp(
    featuredViewModel: FeaturedViewModel = viewModel(factory = FeaturedViewModel.Factory),
    salesViewModel: SalesViewModel = viewModel(factory = SalesViewModel.Factory),
    newsViewModel: NewsViewModel = viewModel(factory = NewsViewModel.Factory),
    searchViewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory),
    appDetailsViewModel: AppDetailsViewModel = viewModel(factory = AppDetailsViewModel.Factory),
    navController: NavHostController = rememberNavController()
) {
    val focusManager = LocalFocusManager.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route.orEmpty()
    val backStackEntry by navController.currentBackStackEntryAsState()
    var canNavigateBack by remember { mutableStateOf(false) }
    val selectedScreen = remember { mutableStateOf(TrackerScreens.Store.name) }
    val searchResults by searchViewModel.searchResults.collectAsState()
    val nameFromId by searchViewModel.nameFromId.collectAsState()
    val searchErrorMessage by searchViewModel.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Dynamically update navigation status based on current navigation destination
    LaunchedEffect(backStackEntry) {
        canNavigateBack = navController.previousBackStackEntry != null
    }

    // Dynamically check for search error messages
    LaunchedEffect(searchErrorMessage) {
        searchErrorMessage?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )

            // Clear the error message so it can repeat if necessary
            searchViewModel.clearError()
        }
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
                navigateUp = { navController.popBackStack() },
                previousBackStackEntry = navController.previousBackStackEntry,
                selectedScreen = selectedScreen
            )
        },
        bottomBar = {
            TrackerBottomAppBar(
                // Prevent bottom app bar navigation from adding back stack entries. Instead,
                // replace the start destination with the current tab.
                destinations = TrackerScreens.entries.associate { screen ->
                    screen.name to { navController.navigate(screen.name) { popUpTo(0) { inclusive = true } } }
                },
                selectedScreen = selectedScreen
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            NavHost(
                navController = navController,
                startDestination = TrackerScreens.Store.name,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
                modifier = Modifier
            ) {
                composable(
                    route = TrackerScreens.Store.name
                ) {
                    StoreScreen(
                        featuredUiState = featuredViewModel.featuredUiState,
                        getFeatured = featuredViewModel::getFeaturedCategories,
                        salesUiState = salesViewModel.salesUiState,
                        getSales = salesViewModel::getSalesGames,
                        searchStore = searchViewModel::getSearchResults,
                        clearSearch = searchViewModel::clearSearchResults,
                        searchResults = searchResults.items
                    )
                }
                composable(
                    route = TrackerScreens.News.name
                ) {
                    NewsScreen(
                        newsUiState = newsViewModel.newsUiState,
                        getNews = newsViewModel::getNews,
                        getNameFromId = searchViewModel::getNameFromId,
                        nameFromId = nameFromId
                    )
                }
                composable(
                    route = TrackerScreens.Collections.name
                ) {
                    // TODO: Implement Collections
                }
                composable(
                    route = TrackerScreens.Notifications.name
                ) {
                    // TODO: Implement Notifications
                }
                composable(
                    route = TrackerScreens.Menu.name
                ) {
                    // TODO: Implement Menu/Settings/Preferences
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
    navigateUp: () -> Unit,
    previousBackStackEntry: NavBackStackEntry?,
    selectedScreen: MutableState<String>
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
                IconButton(onClick = {
                    selectedScreen.value = previousBackStackEntry!!.destination.route!!
                    navigateUp()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}

@Composable
fun TrackerBottomAppBar(
    destinations: Map<String, () -> Unit>,
    selectedScreen: MutableState<String>,
    modifier: Modifier = Modifier
) {
    val icons = mapOf(
        TrackerScreens.Store.name to Icons.Filled.Store,
        TrackerScreens.News.name to Icons.Filled.Newspaper,
        TrackerScreens.Collections.name to Icons.Filled.CollectionsBookmark,
        TrackerScreens.Notifications.name to Icons.Filled.Notifications,
        TrackerScreens.Menu.name to Icons.Filled.Menu
    )

    BottomAppBar(
        modifier = modifier
    ) {
        destinations.forEach { (screen, destination) ->
            val isSelected = selectedScreen.value == (screen)

            IconButton(
                onClick = {
                    if (!isSelected) {
                        selectedScreen.value = screen
                        destination()
                    }
                },
                modifier = modifier.weight(1f)
            ) {
                Icon(
                    icons[screen]!!,
                    contentDescription = screen,
                    tint = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Unspecified
                    }
                )
            }
        }
    }
}
