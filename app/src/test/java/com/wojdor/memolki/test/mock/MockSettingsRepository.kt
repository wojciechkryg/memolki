package com.wojdor.memolki.test.mock

import com.wojdor.memolki.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow

class MockSettingsRepository(
    private val musicEnabled: MutableStateFlow<Boolean> = MutableStateFlow(true),
    private val soundEnabled: MutableStateFlow<Boolean> = MutableStateFlow(true),
    private val vibrationEnabled: MutableStateFlow<Boolean> = MutableStateFlow(true)
) : SettingsRepository(settingsLocalDataSource = MockSettingsLocalDataSource()) {

    override fun getMusicEnabled() = musicEnabled

    override suspend fun setMusicEnabled(value: Boolean) {
        musicEnabled.value = value
    }

    override fun getSoundEnabled() = soundEnabled

    override suspend fun setSoundEnabled(value: Boolean) {
        soundEnabled.value = value
    }

    override fun getVibrationEnabled() = vibrationEnabled

    override suspend fun setVibrationEnabled(value: Boolean) {
        vibrationEnabled.value = value
    }
}
