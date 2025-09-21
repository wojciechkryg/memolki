package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.SettingsRepository
import com.wojdor.memolki.di.coroutine.DefaultDispatcher
import com.wojdor.memolki.domain.model.SettingModel
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    @DefaultDispatcher coroutineDispatcher: CoroutineDispatcher,
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
