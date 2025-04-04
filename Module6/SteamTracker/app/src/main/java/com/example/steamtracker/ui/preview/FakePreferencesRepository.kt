package com.example.steamtracker.ui.preview

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.example.steamtracker.data.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.io.File

class FakePreferencesRepository(): PreferencesRepository {
    override val dataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = {
                File.createTempFile("fake_preferences", ".preferences_pb")
            }
        )

    override suspend fun saveFavoriteGenres(genres: Set<String>) {
    }

    override suspend fun saveFavoriteTags(tags: Set<String>) {
    }

    override fun getFavoriteGenres(): Flow<Set<String>> {
        return flowOf(setOf())
    }

    override fun getFavoriteTags(): Flow<Set<String>> {
        return flowOf(setOf())
    }
}
