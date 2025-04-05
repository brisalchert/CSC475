package com.example.steamtracker.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamtracker.model.FeaturedCategoriesRequest
import com.example.steamtracker.ui.screens.LoadingScreen
import com.example.steamtracker.ui.screens.StoreErrorScreen
import com.example.steamtracker.ui.theme.SteamTrackerTheme

@Composable
fun FeaturedTab(
    featuredUiState: FeaturedUiState,
    getFeatured: () -> Unit,
    navigateApp: () -> Unit,
    onAppSelect: (appId: Int) -> Unit,
    showTopSellers: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    when (featuredUiState) {
        is FeaturedUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is FeaturedUiState.Success -> FeaturedCategoriesList(
            featuredCategories = featuredUiState.featuredCategories,
            navigateApp = navigateApp,
            onAppSelect = onAppSelect,
            showTopSellers = showTopSellers,
            modifier = modifier,
            contentPadding = contentPadding
        )
        is FeaturedUiState.Error -> StoreErrorScreen(
            retryAction = getFeatured,
            modifier = modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FeaturedTabPreview() {
    SteamTrackerTheme {
        FeaturedTab(
            featuredUiState = FeaturedUiState.Success(
                featuredCategories = FeaturedCategoriesRequest()
            ),
            getFeatured = {},
            navigateApp = {},
            onAppSelect = {},
            showTopSellers = true
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeaturedCategoriesList(
    featuredCategories: FeaturedCategoriesRequest,
    navigateApp: () -> Unit,
    onAppSelect: (appId: Int) -> Unit,
    showTopSellers: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    FeaturedGamesList(
        featuredCategories = featuredCategories,
        navigateApp = navigateApp,
        onAppSelect = onAppSelect,
        showTopSellers = showTopSellers,
        modifier = modifier,
        contentPadding = contentPadding
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeaturedGamesList(
    featuredCategories: FeaturedCategoriesRequest,
    navigateApp: () -> Unit,
    onAppSelect: (appId: Int) -> Unit,
    showTopSellers: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
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
                    text = "SPECIALS",
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
            }
        }

        items(items = featuredCategories.specials?.items!!) { game ->
            FeaturedApp(
                appInfo = game,
                navigateApp = navigateApp,
                onAppSelect = onAppSelect,
                modifier = modifier
            )
        }

        item {
            Spacer(
                modifier = modifier
                    .fillMaxWidth()
                    .height(80.dp)
            )
        }

        if (showTopSellers) {
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

            items(items = featuredCategories.topSellers?.items!!) { game ->
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
