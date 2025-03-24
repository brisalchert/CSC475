@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.steamtracker.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.gestures.detectTapGestures
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
import com.example.steamtracker.ui.components.NewsAppsViewModel
import com.example.steamtracker.ui.screens.AppDetailsViewModel
import com.example.steamtracker.ui.screens.StoreScreen
import com.example.steamtracker.ui.components.FeaturedViewModel
import com.example.steamtracker.ui.components.SalesViewModel
import com.example.steamtracker.ui.components.SearchViewModel
import com.example.steamtracker.ui.screens.AppDetailsScreen
import com.example.steamtracker.ui.screens.NewsScreen
import com.example.steamtracker.ui.screens.NewsViewModel
import com.example.steamtracker.ui.screens.SearchScreen

enum class TrackerMainScreens {
    Store,
    News,
    Collections,
    Notifications,
    Menu
}

enum class TrackerOtherScreens {
    Search,
    App
}

@Composable
fun SteamTrackerApp(
    featuredViewModel: FeaturedViewModel = viewModel(factory = FeaturedViewModel.Factory),
    salesViewModel: SalesViewModel = viewModel(factory = SalesViewModel.Factory),
    newsViewModel: NewsViewModel = viewModel(factory = NewsViewModel.Factory),
    searchViewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory),
    appDetailsViewModel: AppDetailsViewModel = viewModel(factory = AppDetailsViewModel.Factory),
    newsAppsViewModel: NewsAppsViewModel = viewModel(factory = NewsAppsViewModel.Factory),
    navController: NavHostController = rememberNavController()
) {
    val focusManager = LocalFocusManager.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route.orEmpty()
    val selectedScreen = remember { mutableStateOf(TrackerMainScreens.Store.name) }

    // Dynamically update navigation status based on current navigation destination
    var canNavigateBack by remember { mutableStateOf(navController.previousBackStackEntry != null) }
    LaunchedEffect(navController.currentBackStackEntryAsState().value) {
        canNavigateBack = navController.previousBackStackEntry != null
    }

    // UI States for search data updates
    val nameFromId by searchViewModel.nameFromId.collectAsState()
    val autocompleteResults by searchViewModel.autocompleteResults.collectAsState()
    val searchResults by searchViewModel.searchResults.collectAsState()
    val searchErrorMessage by searchViewModel.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // UI States for observing live data updates
    val featuredUiState by featuredViewModel.featuredUiState.collectAsState()
    val salesUiState by salesViewModel.salesUiState.collectAsState()
    val newsUiState by newsViewModel.newsUiState.collectAsState()
    val searchUiState by searchViewModel.searchUiState.collectAsState()
    val appDetailsUiState by appDetailsViewModel.appDetailsUiState.collectAsState()

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
                destinations = TrackerMainScreens.entries.associate { screen ->
                    screen.name to {
                        navController.navigate(screen.name) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
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
                startDestination = TrackerMainScreens.Store.name,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
                modifier = Modifier
            ) {
                composable(
                    route = TrackerMainScreens.Store.name
                ) {
                    StoreScreen(
                        featuredUiState = featuredUiState,
                        getFeatured = featuredViewModel::getFeaturedCategories,
                        salesUiState = salesUiState,
                        getSales = salesViewModel::getSalesGames,
                        searchStore = searchViewModel::getAutocompleteResults,
                        clearSearch = searchViewModel::clearSearchResults,
                        autocompleteResults = autocompleteResults.items,
                        navigateSearch = {
                            navController.navigate(TrackerOtherScreens.Search.name) {
                                popUpTo(TrackerOtherScreens.Search.name) { inclusive = true }
                            }
                        },
                        onSearch = searchViewModel::getSearchResults,
                        navigateApp = {
                            navController.navigate(TrackerOtherScreens.App.name) {
                                popUpTo(TrackerOtherScreens.App.name) { inclusive = true }
                            }
                        },
                        onAppSelect = appDetailsViewModel::getAppDetails
                    )
                }
                composable(
                    route = TrackerMainScreens.News.name
                ) {
                    NewsScreen(
                        newsUiState = newsUiState,
                        getNews = newsViewModel::getNews,
                        getNameFromId = searchViewModel::getNameFromId,
                        nameFromId = nameFromId,
                        navigateApp = {
                            navController.navigate(TrackerOtherScreens.App.name) {
                                popUpTo(TrackerOtherScreens.App.name) { inclusive = true }
                            }
                        }
                    )
                }
                composable(
                    route = TrackerMainScreens.Collections.name
                ) {
                    // TODO: Implement Collections
                }
                composable(
                    route = TrackerMainScreens.Notifications.name
                ) {
                    // TODO: Implement Notifications
                }
                composable(
                    route = TrackerMainScreens.Menu.name
                ) {
                    // TODO: Implement Menu/Settings/Preferences
                }
                composable(
                    route = TrackerOtherScreens.Search.name
                ) {
                    SearchScreen(
                        searchUiState = searchUiState,
                        searchStore = searchViewModel::getAutocompleteResults,
                        clearSearch = searchViewModel::clearSearchResults,
                        autocompleteResults = autocompleteResults.items,
                        searchResults = searchResults.items,
                        navigateSearch = {
                            navController.navigate(TrackerOtherScreens.Search.name) {
                                popUpTo(TrackerOtherScreens.Search.name) { inclusive = true }
                            }
                        },
                        onSearch = searchViewModel::getSearchResults,
                        navigateApp = {
                            navController.navigate(TrackerOtherScreens.App.name) {
                                popUpTo(TrackerOtherScreens.App.name) { inclusive = true }
                            }
                        },
                        onAppSelect = appDetailsViewModel::getAppDetails
                    )
                }
                composable(
                    route = TrackerOtherScreens.App.name
                ) {
                    AppDetailsScreen(
                        appDetailsUiState = appDetailsUiState,
                        getAppDetails = appDetailsViewModel::getAppDetails,
                        newsAppsViewModel = newsAppsViewModel,
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
    navigateUp: () -> Unit,
    previousBackStackEntry: NavBackStackEntry?,
    selectedScreen: MutableState<String>
) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.onPrimary,
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
            containerColor = MaterialTheme.colorScheme.primary,
            scrolledContainerColor = MaterialTheme.colorScheme.primary
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
        TrackerMainScreens.Store.name to Icons.Filled.Store,
        TrackerMainScreens.News.name to Icons.Filled.Newspaper,
        TrackerMainScreens.Collections.name to Icons.Filled.CollectionsBookmark,
        TrackerMainScreens.Notifications.name to Icons.Filled.Notifications,
        TrackerMainScreens.Menu.name to Icons.Filled.Menu
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
