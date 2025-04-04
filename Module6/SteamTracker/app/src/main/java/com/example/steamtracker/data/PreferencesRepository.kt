package com.example.steamtracker.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object PreferencesKeys {
    val FAVORITE_GENRES = stringSetPreferencesKey("favorite_genres")
    val FAVORITE_TAGS = stringSetPreferencesKey("favorite_tags")
}

interface PreferencesRepository {
    val dataStore: DataStore<Preferences>

    suspend fun saveFavoriteGenres(genres: Set<String>)
    suspend fun saveFavoriteTags(tags: Set<String>)
    fun getFavoriteGenres(): Flow<Set<String>>
    fun getFavoriteTags(): Flow<Set<String>>
}

class NetworkPreferencesRepository(
    override val dataStore: DataStore<Preferences>
): PreferencesRepository {
    /**
     * Functions for accessing and manipulating the DataStore
     */

    override suspend fun saveFavoriteGenres(genres: Set<String>) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.FAVORITE_GENRES] = genres
        }
    }

    override suspend fun saveFavoriteTags(tags: Set<String>) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.FAVORITE_TAGS] = tags
        }
    }

    override fun getFavoriteGenres(): Flow<Set<String>> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.FAVORITE_GENRES] ?: emptySet()
        }
    }

    override fun getFavoriteTags(): Flow<Set<String>> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.FAVORITE_TAGS] ?: emptySet()
        }
    }
}
