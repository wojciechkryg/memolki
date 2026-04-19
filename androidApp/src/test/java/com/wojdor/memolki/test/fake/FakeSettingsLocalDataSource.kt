package com.wojdor.memolki.test.fake

import com.wojdor.memolki.data.local.datastore.settings.SettingsLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeSettingsLocalDataSource : SettingsLocalDataSource(FakeDataStore()) {

    private val musicEnabled = MutableStateFlow(true)
    private val soundEnabled = MutableStateFlow(true)
    private val vibrationEnabled = MutableStateFlow(true)

    override fun getMusicEnabled(): Flow<Boolean> = musicEnabled

    override suspend fun setMusicEnabled(value: Boolean) {
        musicEnabled.value = value
    }

    override fun getSoundEnabled(): Flow<Boolean> = soundEnabled

    override suspend fun setSoundEnabled(value: Boolean) {
        soundEnabled.value = value
    }

    override fun getVibrationEnabled(): Flow<Boolean> = vibrationEnabled

    override suspend fun setVibrationEnabled(value: Boolean) {
        vibrationEnabled.value = value
    }
}
