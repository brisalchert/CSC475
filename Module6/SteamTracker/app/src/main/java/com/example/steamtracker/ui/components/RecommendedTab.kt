package com.example.steamtracker.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamtracker.model.AppDetails
import com.example.steamtracker.model.AppInfo
import com.example.steamtracker.model.FeaturedCategoriesRequest
import com.example.steamtracker.model.SteamSpyAppRequest
import com.example.steamtracker.ui.screens.LoadingScreen
import com.example.steamtracker.ui.screens.StoreErrorScreen

@Composable
fun RecommendedTab(
    salesUiState: SalesUiState,
    salesAppDetails: List<AppDetails?>,
    featuredUiState: FeaturedUiState,
    preferencesViewModel: PreferencesViewModel,
    getFeatured: () -> Unit,
    navigateApp: () -> Unit,
    onAppSelect: (appId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    when (featuredUiState) {
        is FeaturedUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is FeaturedUiState.Success ->
            when (salesUiState) {
                is SalesUiState.Success -> {
                    PersonalRecommendationsList(
                        salesGames = salesUiState.salesGames,
                        salesAppDetails = salesAppDetails,
                        featuredCategories = featuredUiState.featuredCategories,
                        preferencesViewModel = preferencesViewModel,
                        navigateApp = navigateApp,
                        onAppSelect = onAppSelect,
                        modifier = modifier
                    )
                }
                else -> RecommendedCategoriesList(
                    featuredCategories = featuredUiState.featuredCategories,
                    navigateApp = navigateApp,
                    onAppSelect = onAppSelect,
                    modifier = modifier
                )
            }

        is FeaturedUiState.Error -> StoreErrorScreen(
            retryAction = getFeatured,
            modifier = modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PersonalRecommendationsList(
    salesGames: List<SteamSpyAppRequest>,
    salesAppDetails: List<AppDetails?>,
    featuredCategories: FeaturedCategoriesRequest,
    preferencesViewModel: PreferencesViewModel,
    navigateApp: () -> Unit,
    onAppSelect: (appId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val favoriteGenres by preferencesViewModel.favoriteGenres.collectAsState()
    val favoriteTags by preferencesViewModel.favoriteTags.collectAsState()

    // Pair SteamSpy info with AppDetails
    val salesPairs = salesGames.map { game ->
        Pair(game, salesAppDetails.find { it?.steamAppId == game.appid })
    }

    // Construct the recommended games list using the user's preferences
    // Take the first 15 games, sorted by highest number of recommendations
    val recommendedGames = salesPairs.filter { gamePair ->

        val containsGenre = gamePair.second?.genres?.any { it.description in favoriteGenres } == true
        val containsTag = gamePair.first.tags?.keys?.any { it in favoriteTags } == true

        containsGenre || containsTag
    }.sortedByDescending { it.second?.recommendations?.total }.take(15)

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        stickyHeader {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .shadow(8.dp, RoundedCornerShape(0.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "FOR YOU",
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
            }
        }

        items(items = recommendedGames) { gamePair ->
            SalesApp(
                appInfo = gamePair.first,
                appDetails = gamePair.second,
                navigateApp = navigateApp,
                onAppSelect = onAppSelect,
            )
        }

        stickyHeader {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .shadow(8.dp, RoundedCornerShape(0.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "NEW RELEASES",
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
            }
        }

        featuredCategories.newReleases?.let {
            items(items = featuredCategories.newReleases.items!!) { game ->
                FeaturedApp(
                    appInfo = game,
                    navigateApp = navigateApp,
                    onAppSelect = onAppSelect,
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
fun RecommendedCategoriesList(
    featuredCategories: FeaturedCategoriesRequest,
    navigateApp: () -> Unit,
    onAppSelect: (appId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    featuredCategories.newReleases?.let {
        RecommendedGamesList(
            featuredCategories.newReleases.items!!,
            header = "NEW RELEASES",
            navigateApp = navigateApp,
            onAppSelect = onAppSelect
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecommendedGamesList(
    featuredGames: List<AppInfo>,
    header: String,
    navigateApp: () -> Unit,
    onAppSelect: (appId: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        stickyHeader {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .shadow(8.dp, RoundedCornerShape(0.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = header,
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
            }
        }

        items(items = featuredGames) { game ->
            FeaturedApp(
                appInfo = game,
                navigateApp = navigateApp,
                onAppSelect = onAppSelect,
                modifier = modifier
            )
        }
    }
}
