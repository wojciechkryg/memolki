package com.wojdor.memolki.data.local.datastore.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

open class SettingsLocalDataSource(
    private val dataStore: DataStore<Preferences>
) {

    open fun getMusicEnabled(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[Key.MUSIC_ENABLED] ?: true
    }

    open suspend fun setMusicEnabled(value: Boolean) {
        dataStore.edit { it[Key.MUSIC_ENABLED] = value }
    }

    open fun getSoundEnabled(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[Key.SOUND_ENABLED] ?: true
    }

    open suspend fun setSoundEnabled(value: Boolean) {
        dataStore.edit { it[Key.SOUND_ENABLED] = value }
    }

    open fun getVibrationEnabled(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[Key.VIBRATION_ENABLED] ?: true
    }

    open suspend fun setVibrationEnabled(value: Boolean) {
        dataStore.edit { it[Key.VIBRATION_ENABLED] = value }
    }

    private object Key {
        val MUSIC_ENABLED = booleanPreferencesKey("music_enabled")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
    }
}
