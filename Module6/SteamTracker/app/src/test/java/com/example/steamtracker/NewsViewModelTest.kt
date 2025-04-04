package com.example.steamtracker

import com.example.steamtracker.fake.FakeAppNewsRequest
import com.example.steamtracker.fake.FakeNetworkSteamworksRepository
import com.example.steamtracker.rules.TestDispatcherRule
import com.example.steamtracker.ui.screens.NewsUiState
import com.example.steamtracker.ui.screens.NewsViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NewsViewModelTest {
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private lateinit var newsViewModel: NewsViewModel

    @Before
    fun setup() {
        newsViewModel = NewsViewModel(
            steamworksRepository = FakeNetworkSteamworksRepository(),
            workManager = null
        )
    }

    @Test
    fun newsViewModel_observeTrackedApps_verifyNewsUiStateSuccess() =
        runTest {
            // View model calls observeTrackedApps on init
            assertEquals(
                NewsUiState.Success(
                    newsItems = listOf(FakeAppNewsRequest.response.appnews.newsitems)
                ),
                newsViewModel.newsUiState.first()
            )
        }
}
