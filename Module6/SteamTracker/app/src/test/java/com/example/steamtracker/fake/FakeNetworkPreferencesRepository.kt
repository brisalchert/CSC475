package com.example.steamtracker.fake

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.steamtracker.data.PreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FakeNetworkPreferencesRepository(
    private val testDataStore: DataStore<Preferences>
) {
    /**
     * Functions for accessing and manipulating the DataStore
     */

    suspend fun saveFavoriteGenres(genres: Set<String>) {
        testDataStore.edit { preferences ->
            preferences[PreferencesKeys.FAVORITE_GENRES] = genres
        }
    }

    suspend fun saveFavoriteTags(tags: Set<String>) {
        testDataStore.edit { preferences ->
            preferences[PreferencesKeys.FAVORITE_TAGS] = tags
        }
    }

    fun getFavoriteGenres(): Flow<Set<String>> {
        return testDataStore.data.map { preferences ->
            preferences[PreferencesKeys.FAVORITE_GENRES] ?: emptySet()
        }
    }

    fun getFavoriteTags(): Flow<Set<String>> {
        return testDataStore.data.map { preferences ->
            preferences[PreferencesKeys.FAVORITE_TAGS] ?: emptySet()
        }
    }
}
