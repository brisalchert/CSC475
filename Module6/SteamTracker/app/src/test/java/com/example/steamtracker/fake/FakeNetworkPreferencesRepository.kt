package com.example.steamtracker.fake

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.steamtracker.data.PreferencesKeys
import com.example.steamtracker.data.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FakeNetworkPreferencesRepository(
    override val dataStore: DataStore<Preferences>
): PreferencesRepository {
    /**
     * Functions for accessing and manipulating the DataStore
     */

    override suspend fun saveFavoriteGenres(genres: Set<String>) {
    }

    override suspend fun saveFavoriteTags(tags: Set<String>) {
    }

    override fun getFavoriteGenres(): Flow<Set<String>> {
        return flowOf(setOf("Action", "Adventure"))
    }

    override fun getFavoriteTags(): Flow<Set<String>> {
        return flowOf(setOf("Multiplayer", "Dark Fantasy", "Souls-like"))
    }
}
