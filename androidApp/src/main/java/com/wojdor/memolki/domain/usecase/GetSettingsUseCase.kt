package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.SettingsRepository
import com.wojdor.memolki.domain.model.SettingModel
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.combine

class GetSettingsUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val settingsRepository: SettingsRepository
) : BaseUseCase<List<SettingModel>>(coroutineDispatcher) {

    override fun execute() =
        combine(
            settingsRepository.getMusicEnabled(),
            settingsRepository.getSoundEnabled(),
            settingsRepository.getVibrationEnabled()
        ) { music, sound, vibration ->
            val settings = listOf(
                SettingModel.Music(music),
                SettingModel.Sound(sound),
                SettingModel.Vibration(vibration)
            )
            Result.success(settings)
        }
}
