package com.wojdor.memolki.test.mock

import com.wojdor.memolki.data.local.settings.SettingsLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MockSettingsLocalDataSource : SettingsLocalDataSource(MockDataStore()) {

    private val musicEnabled = MutableStateFlow(true)
    private val soundEnabled = MutableStateFlow(true)
    private val vibrationEnabled = MutableStateFlow(true)

    override fun getMusicEnabled(): Flow<Boolean> = musicEnabled

    override suspend fun setMusicEnabled(isEnabled: Boolean) {
        musicEnabled.value = isEnabled
    }

    override fun getSoundEnabled(): Flow<Boolean> = soundEnabled

    override suspend fun setSoundEnabled(isEnabled: Boolean) {
        soundEnabled.value = isEnabled
    }

    override fun getVibrationEnabled(): Flow<Boolean> = vibrationEnabled

    override suspend fun setVibrationEnabled(isEnabled: Boolean) {
        vibrationEnabled.value = isEnabled
    }
}
