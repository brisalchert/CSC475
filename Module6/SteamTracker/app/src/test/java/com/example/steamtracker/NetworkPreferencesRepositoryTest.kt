package com.example.steamtracker

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.example.steamtracker.data.NetworkPreferencesRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import java.io.File

class NetworkPreferencesRepositoryTest {
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var repository: NetworkPreferencesRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        dataStore = PreferenceDataStoreFactory.create(
            produceFile = {
                File.createTempFile("test_preferences", ".preferences_pb")
            }
        )
        repository = NetworkPreferencesRepository(
            dataStore
        )
    }

    @Test
    fun preferencesRepository_verifySaveAndRetrieveFavoriteGenres() =
        runTest {
            val fakeGenreSet = setOf("Action", "Adventure")
            repository.saveFavoriteGenres(fakeGenreSet)

            assertEquals(
                flowOf(fakeGenreSet).first(),
                repository.getFavoriteGenres().first()
            )
        }

    @Test
    fun preferencesRepository_verifySaveAndRetrieveFavoriteTags() =
        runTest {
            val fakeTagSet = setOf("Multiplayer", "Dark Fantasy", "Souls-like")
            repository.saveFavoriteTags(fakeTagSet)

            assertEquals(
                flowOf(fakeTagSet).first(),
                repository.getFavoriteTags().first()
            )
        }
}
