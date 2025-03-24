package com.example.steamtracker.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamtracker.model.AppInfo
import com.example.steamtracker.model.FeaturedCategoriesRequest
import com.example.steamtracker.ui.screens.LoadingScreen
import com.example.steamtracker.ui.screens.StoreErrorScreen

@Composable
fun FeaturedTab(
    featuredUiState: FeaturedUiState,
    getFeatured: () -> Unit,
    navigateApp: () -> Unit,
    onAppSelect: (appId: Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    when (featuredUiState) {
        is FeaturedUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is FeaturedUiState.Success -> FeaturedCategoriesList(
            featuredCategories = featuredUiState.featuredCategories,
            navigateApp = navigateApp,
            onAppSelect = onAppSelect,
            modifier = modifier,
            contentPadding = contentPadding
        )
        is FeaturedUiState.Error -> StoreErrorScreen(
            retryAction = getFeatured,
            modifier = modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeaturedCategoriesList(
    featuredCategories: FeaturedCategoriesRequest,
    navigateApp: () -> Unit,
    onAppSelect: (appId: Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        featuredCategories.specials?.let {
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
                        text = "SPECIALS",
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                }
            }
            item {
                FeaturedGamesList(
                    featuredCategories.specials.items!!,
                    navigateApp = navigateApp,
                    onAppSelect = onAppSelect
                )
            }
        }

        item {
            Spacer(
                modifier = modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
        }

        featuredCategories.topSellers?.let {
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
                        text = "TOP SELLERS",
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                }
            }
            item {
                FeaturedGamesList(
                    featuredCategories.topSellers.items!!,
                    navigateApp = navigateApp,
                    onAppSelect = onAppSelect
                )
            }
        }
    }
}

@Composable
fun FeaturedGamesList(
    featuredGames: List<AppInfo>,
    navigateApp: () -> Unit,
    onAppSelect: (appId: Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val seenIds = mutableSetOf<Int>()

        featuredGames.forEach { game ->
            // Prevent duplicate entries in the list
            if (!seenIds.contains(game.id)) {
                seenIds.add(game.id)

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
