package com.wojdor.memolki.ui.feature.settings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.model.SettingModel
import com.wojdor.memolki.domain.usecase.GetSettingsUseCase
import com.wojdor.memolki.domain.usecase.ToggleSettingsUseCase
import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.util.media.BackgroundMusicPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val hapticFeedback: HapticFeedback,
    private val backgroundMusicPlayer: BackgroundMusicPlayer,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val toggleSettingsUseCase: ToggleSettingsUseCase
) : MviViewModel<SettingsIntent, SettingsState>(
    savedStateHandle,
    SettingsState()
) {

    init {
        loadSettings()
    }

    override fun onIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.OnSettingClick -> {
                toggleSettingsUseCase(intent.setting).onEach {
                    it.onSuccess { setting -> handleTogglingSetting(setting) }
                }.launchIn(viewModelScope)
            }
            is SettingsIntent.OnLanguageClick -> sendEffect(SettingsEffect.OpenChangeLanguageScreen)
        }
    }

    private fun handleTogglingSetting(setting: SettingModel) {
        when (setting) {
            is SettingModel.Music -> handleMusicSetting(setting)
            else -> Unit
        }
        hapticFeedback.vibrateLow()
        updateSettingState(setting)
    }

    private fun handleMusicSetting(setting: SettingModel.Music) {
        if (setting.isEnabled) {
            backgroundMusicPlayer.start()
        } else {
            backgroundMusicPlayer.pause()
        }
    }

    private fun updateSettingState(setting: SettingModel) {
        sendState {
            copy(settings = settings.map {
                if (it::class == setting::class) {
                    setting
                } else {
                    it
                }
            })
        }
    }

    private fun loadSettings() {
        getSettingsUseCase().onEach {
            it.onSuccess { settings ->
                sendState { copy(settings = settings) }
            }
        }.launchIn(viewModelScope)
    }
}
