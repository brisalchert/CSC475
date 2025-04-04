package com.example.steamtracker

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.example.steamtracker.fake.FakeNetworkPreferencesRepository
import com.example.steamtracker.rules.TestDispatcherRule
import com.example.steamtracker.ui.components.PreferencesViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations
import java.io.File

class PreferencesViewModelTest {
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var preferencesViewModel: PreferencesViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        dataStore = PreferenceDataStoreFactory.create(
            produceFile = {
                File.createTempFile("test_preferences", ".preferences_pb")
            }
        )
        preferencesViewModel = PreferencesViewModel(
            preferencesRepository = FakeNetworkPreferencesRepository(dataStore)
        )
    }

    @Test
    fun preferencesViewModel_observeGenres_verifyGenresCorrect() =
        runTest {
            // View model calls observeGenres() on init
            assertEquals(
                setOf("Action", "Adventure"),
                preferencesViewModel.favoriteGenres.value
            )
        }

    @Test
    fun preferencesViewModel_observeTags_verifyTagsCorrect() =
        runTest {
            // View model calls observeTags() on init
            assertEquals(
                setOf("Multiplayer", "Dark Fantasy", "Souls-like"),
                preferencesViewModel.favoriteTags.value
            )
        }
}
