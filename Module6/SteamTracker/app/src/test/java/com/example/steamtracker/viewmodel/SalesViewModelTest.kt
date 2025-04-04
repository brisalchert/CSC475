package com.example.steamtracker.viewmodel

import com.example.steamtracker.fake.FakeAppDetailsRequest
import com.example.steamtracker.fake.FakeNetworkSpyRepository
import com.example.steamtracker.fake.FakeNetworkStoreRepository
import com.example.steamtracker.fake.FakeSteamSpyAppRequest
import com.example.steamtracker.rules.TestDispatcherRule
import com.example.steamtracker.ui.components.SalesUiState
import com.example.steamtracker.ui.components.SalesViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SalesViewModelTest {
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private lateinit var salesViewModel: SalesViewModel

    @Before
    fun setup() {
        salesViewModel = SalesViewModel(
            spyRepository = FakeNetworkSpyRepository(),
            storeRepository = FakeNetworkStoreRepository()
        )
    }

    @Test
    fun salesViewModel_getSalesGames_verifySalesUiStateSuccess() =
        runTest {
            // View model calls getSalesGames() on init
            assertEquals(
                SalesUiState.Success(
                    salesGames = listOf(FakeSteamSpyAppRequest.response["gameId"]!!)
                ),
                salesViewModel.salesUiState.first()
            )
        }

    @Test
    fun salesViewModel_getSalesAppDetails_verifySalesAppDetailsCorrect() =
        runTest {
            // View model calls getSalesAppDetails() on init
            assertEquals(
                listOf(FakeAppDetailsRequest.response["0"]!!.appDetails),
                salesViewModel.salesAppDetails.first()
            )
        }
}
