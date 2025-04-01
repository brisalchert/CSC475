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
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.steamtracker.R
import com.example.steamtracker.SteamTrackerApplication
import com.example.steamtracker.data.AppInitializer
import com.example.steamtracker.ui.components.FeaturedViewModel
import com.example.steamtracker.ui.components.NewsAppsViewModel
import com.example.steamtracker.ui.components.PreferencesViewModel
import com.example.steamtracker.ui.components.SalesViewModel
import com.example.steamtracker.ui.components.SearchViewModel
import com.example.steamtracker.ui.screens.AppDetailsScreen
import com.example.steamtracker.ui.screens.AppDetailsViewModel
import com.example.steamtracker.ui.screens.CollectionListScreen
import com.example.steamtracker.ui.screens.CollectionSearchScreen
import com.example.steamtracker.ui.screens.CollectionsScreen
import com.example.steamtracker.ui.screens.CollectionsViewModel
import com.example.steamtracker.ui.screens.ImageScreen
import com.example.steamtracker.ui.screens.ImageViewModel
import com.example.steamtracker.ui.screens.MenuScreen
import com.example.steamtracker.ui.screens.NewsDetailsScreen
import com.example.steamtracker.ui.screens.NewsScreen
import com.example.steamtracker.ui.screens.NewsViewModel
import com.example.steamtracker.ui.screens.NotificationsScreen
import com.example.steamtracker.ui.screens.NotificationsViewModel
import com.example.steamtracker.ui.screens.PreferencesScreen
import com.example.steamtracker.ui.screens.SearchScreen
import com.example.steamtracker.ui.screens.SettingsScreen
import com.example.steamtracker.ui.screens.StoreScreen
import com.example.steamtracker.ui.screens.ThemeViewModel

enum class TrackerMainScreens {
    Store,
    News,
    Collections,
    Notifications,
    Menu
}

enum class TrackerOtherScreens {
    Search,
    App,
    Collection,
    CollectionSearch,
    NewsDetails,
    Settings,
    Image,
    Preferences
}

@Composable
fun SteamTrackerApp(
    featuredViewModel: FeaturedViewModel = viewModel(factory = FeaturedViewModel.Factory),
    salesViewModel: SalesViewModel = viewModel(factory = SalesViewModel.Factory),
    newsViewModel: NewsViewModel = viewModel(factory = NewsViewModel.Factory),
    searchViewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory),
    appDetailsViewModel: AppDetailsViewModel = viewModel(factory = AppDetailsViewModel.Factory),
    newsAppsViewModel: NewsAppsViewModel = viewModel(factory = NewsAppsViewModel.Factory),
    collectionsViewModel: CollectionsViewModel = viewModel(factory = CollectionsViewModel.Factory),
    notificationsViewModel: NotificationsViewModel = viewModel(factory = NotificationsViewModel.Factory),
    themeViewModel: ThemeViewModel = viewModel(factory = ThemeViewModel.Factory),
    imageViewModel: ImageViewModel = viewModel(),
    preferencesViewModel: PreferencesViewModel = viewModel(factory = PreferencesViewModel.Factory),
    navController: NavHostController = rememberNavController()
) {
    // Tab index for store screen
    var tabIndex by rememberSaveable { mutableIntStateOf(0) }

    val focusManager = LocalFocusManager.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedScreen = remember { mutableStateOf(navBackStackEntry?.destination?.route ?: TrackerMainScreens.Store.name) }

    // Keep track of selected screen
    LaunchedEffect(navBackStackEntry) {
        selectedScreen.value = navBackStackEntry?.destination?.route ?: TrackerMainScreens.Store.name
    }

    // Dynamically update navigation status based on current navigation destination
    var canNavigateBack by remember { mutableStateOf(navController.previousBackStackEntry != null) }
    LaunchedEffect(navController.currentBackStackEntryAsState().value) {
        canNavigateBack = navController.previousBackStackEntry != null
    }

    // UI States for search data updates
    val autocompleteResults by searchViewModel.autocompleteResults.collectAsState()
    val searchResults by searchViewModel.searchResults.collectAsState()
    val searchErrorMessage by searchViewModel.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // States for news page
    val trackedAppsDetails by newsAppsViewModel.trackedAppDetails.collectAsState()
    val currentNews by newsAppsViewModel.currentNews.collectAsState()

    // AppDetails state for sales page
    val salesAppDetails by salesViewModel.salesAppDetails.collectAsState()

    // States for collections page
    val collectionsAppDetails by collectionsViewModel.collectionsAppsDetails.collectAsState()
    val currentCollection by collectionsViewModel.currentCollection.collectAsStateWithLifecycle()

    // State for screenshot view
    val screenshot by imageViewModel.screenshot.collectAsState()

    // UI States for observing live data updates
    val featuredUiState by featuredViewModel.featuredUiState.collectAsState()
    val salesUiState by salesViewModel.salesUiState.collectAsState()
    val newsUiState by newsViewModel.newsUiState.collectAsState()
    val searchUiState by searchViewModel.searchUiState.collectAsState()
    val appDetailsUiState by appDetailsViewModel.appDetailsUiState.collectAsState()
    val collectionsUiState by collectionsViewModel.collectionsUiState.collectAsState()
    val notificationsUiState by notificationsViewModel.notificationsUiState.collectAsState()

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

    // App Initializer for first launch
    val context = LocalContext.current
    val appContainer = (context.applicationContext as SteamTrackerApplication).container
    val appInitializer = AppInitializer(context, appContainer)

    // SharedPreferences for simple settings
    val isDarkMode by themeViewModel.isDarkMode.collectAsState()

    // Ensure database wiped on first launch
    LaunchedEffect(Unit) {
        appInitializer.checkAndClearDatabase()
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
                        tabIndex = tabIndex,
                        onTabChange = { tabIndex = it },
                        featuredUiState = featuredUiState,
                        getFeatured = featuredViewModel::getFeaturedCategories,
                        salesUiState = salesUiState,
                        getSales = {
                            salesViewModel.getSalesGames()
                            salesViewModel.getSalesAppDetails()
                        },
                        salesAppDetails = salesAppDetails,
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
                        trackedAppsDetails = trackedAppsDetails,
                        navigateNews = {
                            navController.navigate(TrackerOtherScreens.NewsDetails.name) {
                                popUpTo(TrackerOtherScreens.NewsDetails.name) { inclusive = true }
                            }
                        },
                        onNewsSelected = newsAppsViewModel::setCurrentNews,
                    )
                }
                composable(
                    route = TrackerMainScreens.Collections.name
                ) {
                    CollectionsScreen(
                        collectionsViewModel = collectionsViewModel,
                        collectionsUiState = collectionsUiState,
                        collectionsAppDetails = collectionsAppDetails,
                        navigateCollection = {
                            navController.navigate(TrackerOtherScreens.Collection.name) {
                                popUpTo(TrackerOtherScreens.Collection.name) { inclusive = true }
                            }
                        },
                        onCollectionSelect = collectionsViewModel::setCollection
                    )
                }
                composable(
                    route = TrackerMainScreens.Notifications.name
                ) {
                    NotificationsScreen(
                        notificationsUiState = notificationsUiState,
                        trackedAppsDetails = trackedAppsDetails,
                        navigateNews = {
                            navController.navigate(TrackerOtherScreens.NewsDetails.name) {
                                popUpTo(TrackerOtherScreens.NewsDetails.name) { inclusive = true }
                            }
                        },
                        onNewsSelected = newsAppsViewModel::setCurrentNews,
                        navigateApp = {
                            navController.navigate(TrackerOtherScreens.App.name) {
                                popUpTo(TrackerOtherScreens.App.name) { inclusive = true }
                            }
                        },
                        onAppSelect = appDetailsViewModel::getAppDetails,
                        onNewsRemoved = notificationsViewModel::deleteNewsNotification,
                        onWishlistRemoved = notificationsViewModel::deleteWishlistNotification
                    )
                }
                composable(
                    route = TrackerMainScreens.Menu.name
                ) {
                    val navMap = mapOf(
                        TrackerMainScreens.Store.name to {
                            navController.navigate(
                                TrackerMainScreens.Store.name
                            ) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        TrackerMainScreens.News.name to {
                            navController.navigate(
                                TrackerMainScreens.News.name
                            ) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        TrackerMainScreens.Collections.name to {
                            navController.navigate(
                                TrackerMainScreens.Collections.name
                            ) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        TrackerMainScreens.Notifications.name to {
                            navController.navigate(
                                TrackerMainScreens.Notifications.name
                            ) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        TrackerOtherScreens.Search.name to {
                            navController.navigate(
                                TrackerOtherScreens.Search.name
                            )
                        },
                        TrackerOtherScreens.Preferences.name to {
                             navController.navigate(
                                 TrackerOtherScreens.Preferences.name
                             )
                        },
                        TrackerOtherScreens.Settings.name to {
                            navController.navigate(
                                TrackerOtherScreens.Settings.name
                            )
                        }
                    )

                    MenuScreen(navigation = navMap)
                }
                composable(
                    route = TrackerOtherScreens.Search.name
                ) {
                    SearchScreen(
                        searchUiState = searchUiState,
                        getAutocomplete = searchViewModel::getAutocompleteResults,
                        clearSearch = searchViewModel::clearSearchResults,
                        autocompleteResults = autocompleteResults.items,
                        searchResults = searchResults.items,
                        sortResults = searchViewModel::sortResults,
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
                        collectionsViewModel = collectionsViewModel,
                        preferencesViewModel = preferencesViewModel,
                        navigateScreenshot = {
                            navController.navigate(TrackerOtherScreens.Image.name) {
                                popUpTo(TrackerOtherScreens.Image.name) { inclusive = true }
                            }
                        },
                        onScreenshotSelect = imageViewModel::setScreenshot
                    )
                }
                composable(
                    route = TrackerOtherScreens.Collection.name
                ) {
                    CollectionListScreen(
                        collectionsViewModel = collectionsViewModel,
                        collection = currentCollection,
                        collectionAppDetails = collectionsAppDetails,
                        navigateApp = {
                            navController.navigate(TrackerOtherScreens.App.name) {
                                popUpTo(TrackerOtherScreens.App.name) { inclusive = true }
                            }
                        },
                        onAppSelect = appDetailsViewModel::getAppDetails,
                        navigateAddApp = {
                            navController.navigate(TrackerOtherScreens.CollectionSearch.name) {
                                popUpTo(TrackerOtherScreens.CollectionSearch.name) { inclusive = true }
                            }
                        }
                    )
                }
                composable(
                    route = TrackerOtherScreens.CollectionSearch.name
                ) {
                    CollectionSearchScreen(
                        collectionsViewModel = collectionsViewModel,
                        currentCollection = currentCollection,
                        onAddApp = collectionsViewModel::addCollectionApp,
                        onRemoveApp = collectionsViewModel::removeCollectionApp,
                        searchUiState = searchUiState,
                        getAutocomplete = searchViewModel::getAutocompleteResults,
                        clearSearch = searchViewModel::clearSearchResults,
                        autocompleteResults = autocompleteResults.items,
                        searchResults = searchResults.items,
                        navigateSearch = {
                            navController.navigate(TrackerOtherScreens.CollectionSearch.name) {
                                popUpTo(TrackerOtherScreens.CollectionSearch.name) { inclusive = true }
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
                    route = TrackerOtherScreens.NewsDetails.name
                ) {
                    NewsDetailsScreen(
                        news = currentNews,
                        trackedAppsDetails = trackedAppsDetails
                    )
                }
                composable(
                    route = TrackerOtherScreens.Settings.name
                ) {
                    SettingsScreen(
                        isDarkMode = isDarkMode,
                        onToggleTheme = themeViewModel::toggleTheme
                    )
                }
                composable(
                    route = TrackerOtherScreens.Image.name
                ) {
                    ImageScreen(
                        screenshot = screenshot
                    )
                }
                composable(
                    route = TrackerOtherScreens.Preferences.name
                ) {
                    PreferencesScreen(
                        preferencesViewModel = preferencesViewModel
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
                style = MaterialTheme.typography.headlineMedium
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
                        contentDescription = stringResource(R.string.back_button),
                        tint = MaterialTheme.colorScheme.onPrimary
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
