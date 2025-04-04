package com.example.steamtracker

import com.example.steamtracker.fake.FakeAppDetailsRequest
import com.example.steamtracker.fake.FakeAppNewsRequest
import com.example.steamtracker.fake.FakeNetworkSteamworksRepository
import com.example.steamtracker.fake.FakeNetworkStoreRepository
import com.example.steamtracker.rules.TestDispatcherRule
import com.example.steamtracker.ui.components.NewsAppsViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NewsAppsViewModelTest {
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private lateinit var newsAppsViewModel: NewsAppsViewModel

    @Before
    fun setup() {
        newsAppsViewModel = NewsAppsViewModel(
            steamworksRepository = FakeNetworkSteamworksRepository(),
            storeRepository = FakeNetworkStoreRepository()
        )
    }

    @Test
    fun newsAppsViewModel_getTrackedAppsDetails_verifyTrackedAppsDetails() =
        runTest {
            // View model calls getTrackedAppsDetails() on init
            assertEquals(
                listOf(FakeAppDetailsRequest.response["0"]!!.appDetails),
                newsAppsViewModel.trackedAppDetails.first()
            )
        }

    @Test
    fun newsAppsViewModel_setCurrentNews_verifyCurrentNews() =
        runTest {
            newsAppsViewModel.setCurrentNews(
                FakeAppNewsRequest.response.appnews.newsitems.first().gid
            )

            assertEquals(
                FakeAppNewsRequest.response.appnews.newsitems.first(),
                newsAppsViewModel.currentNews.first()
            )
        }

    @Test
    fun newsAppsViewModel_getNewsByGid_verifyNewsItem() =
        runTest {
            assertEquals(
                FakeAppNewsRequest.response.appnews.newsitems.first(),
                newsAppsViewModel.getNewsByGid("0")
            )
        }

    @Test
    fun newsAppsViewModel_isAppTracked_verifyTrackedAppList() =
        runTest {
            assertEquals(
                true,
                newsAppsViewModel.isAppTracked(0).first()
            )
        }
}
