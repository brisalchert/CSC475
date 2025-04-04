package com.example.steamtracker.viewmodel

import com.example.steamtracker.fake.FakeFeaturedCategoriesRequest
import com.example.steamtracker.fake.FakeNetworkStoreRepository
import com.example.steamtracker.rules.TestDispatcherRule
import com.example.steamtracker.ui.components.FeaturedUiState
import com.example.steamtracker.ui.components.FeaturedViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class FeaturedViewModelTest {
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun featuredViewModel_getFeaturedCategories_verifyFeaturedUiStateSuccess() =
        runTest {
            // View model calls getFeaturedCategories on init
            val featuredViewModel = FeaturedViewModel(
                storeRepository = FakeNetworkStoreRepository()
            )

            assertEquals(
                FeaturedUiState.Success(
                    featuredCategories = FakeFeaturedCategoriesRequest.response
                ),
                featuredViewModel.featuredUiState.first()
            )
        }
}
