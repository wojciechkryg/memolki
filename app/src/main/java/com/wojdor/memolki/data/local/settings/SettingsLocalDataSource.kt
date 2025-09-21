package com.wojdor.memolki.data.local.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    fun getMusicEnabled(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[Key.MUSIC_ENABLED]?.toBoolean() ?: true
    }

    suspend fun setMusicEnabled(value: Boolean) {
        dataStore.edit { it[Key.MUSIC_ENABLED] = value.toString() }
    }

    fun getSoundEnabled(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[Key.SOUND_ENABLED]?.toBoolean() ?: true
    }

    suspend fun setSoundEnabled(value: Boolean) {
        dataStore.edit { it[Key.SOUND_ENABLED] = value.toString() }
    }

    fun getVibrationEnabled(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[Key.VIBRATION_ENABLED]?.toBoolean() ?: true
    }

    suspend fun setVibrationEnabled(value: Boolean) {
        dataStore.edit { it[Key.VIBRATION_ENABLED] = value.toString() }
    }

    private object Key {
        val MUSIC_ENABLED = stringPreferencesKey("music_enabled")
        val SOUND_ENABLED = stringPreferencesKey("sound_enabled")
        val VIBRATION_ENABLED = stringPreferencesKey("vibration_enabled")
    }
}
