package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.SettingsRepository
import com.wojdor.memolki.di.coroutine.DefaultDispatcher
import com.wojdor.memolki.domain.model.SettingModel
import com.wojdor.memolki.domain.usecase.base.BaseParameterUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ToggleSettingsUseCase @Inject constructor(
    @DefaultDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val settingsRepository: SettingsRepository
) : BaseParameterUseCase<SettingModel, SettingModel>(coroutineDispatcher) {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(setting: SettingModel) = flow {
        val toggledSetting = when (setting) {
            is SettingModel.Music -> setting.copy(isEnabled = !setting.isEnabled)
            is SettingModel.Sound -> setting.copy(isEnabled = !setting.isEnabled)
            is SettingModel.Vibration -> setting.copy(isEnabled = !setting.isEnabled)
        }
        when (setting) {
            is SettingModel.Music -> settingsRepository.setMusicEnabled(toggledSetting.isEnabled)
            is SettingModel.Sound -> settingsRepository.setSoundEnabled(toggledSetting.isEnabled)
            is SettingModel.Vibration -> settingsRepository.setVibrationEnabled(toggledSetting.isEnabled)
        }
        emit(Result.success(toggledSetting))
    }
}
