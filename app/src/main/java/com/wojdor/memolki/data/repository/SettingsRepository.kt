package com.wojdor.memolki.data.repository

import com.wojdor.memolki.data.local.settings.SettingsLocalDataSource
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val settingsLocalDataSource: SettingsLocalDataSource
) {

    fun getMusicEnabled() = settingsLocalDataSource.getMusicEnabled()

    suspend fun setMusicEnabled(value: Boolean) = settingsLocalDataSource.setMusicEnabled(value)

    fun getSoundEnabled() = settingsLocalDataSource.getSoundEnabled()

    suspend fun setSoundEnabled(value: Boolean) = settingsLocalDataSource.setSoundEnabled(value)

    fun getVibrationEnabled() = settingsLocalDataSource.getVibrationEnabled()

    suspend fun setVibrationEnabled(value: Boolean) =
        settingsLocalDataSource.setVibrationEnabled(value)
}
