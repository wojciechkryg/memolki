package com.wojdor.memolki.data.local.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SettingsLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun getMusicEnabled(): Boolean {
        val preferences = dataStore.data.first()
        return preferences[Key.MUSIC_ENABLED]?.toBoolean() ?: true
    }

    suspend fun setMusicEnabled(value: Boolean) {
        dataStore.edit { it[Key.MUSIC_ENABLED] = value.toString() }
    }

    suspend fun getSoundEnabled(): Boolean {
        val preferences = dataStore.data.first()
        return preferences[Key.SOUND_ENABLED]?.toBoolean() ?: true
    }

    suspend fun setSoundEnabled(value: Boolean) {
        dataStore.edit { it[Key.SOUND_ENABLED] = value.toString() }
    }

    suspend fun getVibrationsEnabled(): Boolean {
        val preferences = dataStore.data.first()
        return preferences[Key.VIBRATIONS_ENABLED]?.toBoolean() ?: true
    }

    suspend fun setVibrationsEnabled(value: Boolean) {
        dataStore.edit { it[Key.VIBRATIONS_ENABLED] = value.toString() }
    }

    private object Key {
        val MUSIC_ENABLED = stringPreferencesKey("music_enabled")
        val SOUND_ENABLED = stringPreferencesKey("sound_enabled")
        val VIBRATIONS_ENABLED = stringPreferencesKey("vibrations_enabled")
    }
}
