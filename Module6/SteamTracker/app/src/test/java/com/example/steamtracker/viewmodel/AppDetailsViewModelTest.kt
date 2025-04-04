package com.example.steamtracker.viewmodel

import com.example.steamtracker.fake.FakeAppDetailsRequest
import com.example.steamtracker.fake.FakeNetworkAppDetailsRepository
import com.example.steamtracker.fake.FakeNetworkSpyRepository
import com.example.steamtracker.fake.FakeNetworkStoreRepository
import com.example.steamtracker.fake.FakeSteamSpyAppRequest
import com.example.steamtracker.rules.TestDispatcherRule
import com.example.steamtracker.ui.screens.AppDetailsUiState
import com.example.steamtracker.ui.screens.AppDetailsViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class AppDetailsViewModelTest {
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun appDetailsViewModel_getAppDetails_verifyAppDetailsUiStateSuccess() =
        runTest {
            val appDetailsViewModel = AppDetailsViewModel(
                appDetailsRepository = FakeNetworkAppDetailsRepository(),
                storeRepository = FakeNetworkStoreRepository(),
                spyRepository = FakeNetworkSpyRepository()
            )
            val appId = 0

            // Initialize view model
            appDetailsViewModel.getAppDetails(appId)

            assertEquals(
                AppDetailsUiState.Success(
                    appDetails = FakeAppDetailsRequest.response["$appId"]!!.appDetails,
                    appSpyInfo = FakeSteamSpyAppRequest.response["gameId"]!!,
                    appId = appId
                ),
                appDetailsViewModel.appDetailsUiState.first()
            )
        }
}
