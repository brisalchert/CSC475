package com.example.steamtracker.data

import android.util.Log
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

class PreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    /**
     * Functions for accessing and manipulating the DataStore
     */

    suspend fun saveFavoriteGenres(genres: Set<String>) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.FAVORITE_GENRES] = genres
        }
        Log.d("Preferences", "Current DataStore: ${dataStore.data}")
    }

    suspend fun saveFavoriteTags(tags: Set<String>) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.FAVORITE_TAGS] = tags
        }
        Log.d("Preferences", "Current DataStore: ${dataStore.data}")
    }

    fun getFavoriteGenres(): Flow<Set<String>> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.FAVORITE_GENRES] ?: emptySet()
        }
    }

    fun getFavoriteTags(): Flow<Set<String>> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.FAVORITE_TAGS] ?: emptySet()
        }
    }
}
