package com.example.steamtracker.viewmodel

import com.example.steamtracker.fake.FakeNetworkStoreRepository
import com.example.steamtracker.fake.FakeStoreSearchRequest
import com.example.steamtracker.rules.TestDispatcherRule
import com.example.steamtracker.ui.components.SearchUiState
import com.example.steamtracker.ui.components.SearchViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private lateinit var searchViewModel: SearchViewModel

    @Before
    fun setup() {
        searchViewModel = SearchViewModel(
            storeRepository = FakeNetworkStoreRepository()
        )
    }

    @Test
    fun searchViewModel_getAutocompleteResults_verifyAutocompleteResultsCorrect() =
        runTest {
            searchViewModel.getAutocompleteResults("")

            assertEquals(
                FakeStoreSearchRequest.response,
                searchViewModel.autocompleteResults.first()
            )
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun searchViewModel_getSearchResults_verifySearchUiStateSuccess() =
        runTest {
            searchViewModel.getSearchResults("")

            advanceUntilIdle() // Wait for UI update

            assertEquals(
                SearchUiState.Success(FakeStoreSearchRequest.response.items),
                searchViewModel.searchUiState.first()
            )

            assertEquals(
                FakeStoreSearchRequest.response,
                searchViewModel.searchResults.first()
            )
        }
}
