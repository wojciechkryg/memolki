package com.wojdor.memolki.data.repository

import com.wojdor.memolki.data.local.settings.SettingsLocalDataSource
import javax.inject.Inject

open class SettingsRepository @Inject constructor(
    private val settingsLocalDataSource: SettingsLocalDataSource
) {

    open fun getMusicEnabled() = settingsLocalDataSource.getMusicEnabled()

    open suspend fun setMusicEnabled(value: Boolean) =
        settingsLocalDataSource.setMusicEnabled(value)

    open fun getSoundEnabled() = settingsLocalDataSource.getSoundEnabled()

    open suspend fun setSoundEnabled(value: Boolean) =
        settingsLocalDataSource.setSoundEnabled(value)

    open fun getVibrationEnabled() = settingsLocalDataSource.getVibrationEnabled()

    open suspend fun setVibrationEnabled(value: Boolean) =
        settingsLocalDataSource.setVibrationEnabled(value)
}
