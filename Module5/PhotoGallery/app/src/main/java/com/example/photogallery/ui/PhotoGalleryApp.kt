@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.photogallery.ui

import android.net.Uri
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.photogallery.R
import com.example.photogallery.ui.screens.HomeScreen
import com.example.photogallery.ui.screens.GalleryViewModel
import com.example.photogallery.ui.screens.ImageScreen

/**
 * Enum for navigation destinations. Helps to prevent typing errors when coding
 * navigation features.
 */
enum class GalleryScreen {
    Home,
    Image
}

@Composable
fun PhotoGalleryApp(
    galleryViewModel: GalleryViewModel = viewModel(factory = GalleryViewModel.Factory),
    navController: NavHostController = rememberNavController()
) {
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
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            GalleryTopAppBar(
                scrollBehavior = scrollBehavior,
                canNavigateBack = canNavigateBack,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            NavHost(
                navController = navController,
                startDestination = GalleryScreen.Home.name,
                modifier = Modifier
            ) {
                composable(
                    route = GalleryScreen.Home.name
                ) {
                    HomeScreen(
                        galleryUiState = galleryViewModel.galleryUiState,
                        retryAction = galleryViewModel::getGamePhotos,
                        onImageClicked = { imageUri ->
                            // Prevent repeat navigation to image screen instances
                            if (!currentDestination.startsWith(GalleryScreen.Image.name)) {
                                // Reset Top App Bar scrolling before navigating
                                scrollBehavior.state.heightOffset = 0f


                                navController.navigate("${GalleryScreen.Image.name}/${Uri.encode(imageUri)}") {
                                    popUpTo(GalleryScreen.Image.name) { inclusive = true }
                                }
                            }
                        },
                        contentPadding = PaddingValues()
                    )
                }
                composable(
                    route = "${GalleryScreen.Image.name}/{imageUri}",
                    arguments = listOf(navArgument("imageUri") { type = NavType.StringType })
                ) { backStackEntry ->
                    // Create a backStackEntry to enable the back arrow on the top app bar
                    val imageUri = backStackEntry.arguments?.getString("imageUri")?: ""
                    ImageScreen(
                        imageUri = imageUri
                    )
                }
            }
        }
    }
}

@Composable
fun GalleryTopAppBar(
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
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) { // Only allow back navigation if an image is fullscreen
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
