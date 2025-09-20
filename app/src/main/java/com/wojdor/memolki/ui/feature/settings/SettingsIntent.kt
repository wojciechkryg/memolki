package com.wojdor.memolki.ui.feature.settings

import com.wojdor.memolki.domain.model.SettingModel
import com.wojdor.memolki.ui.base.UiIntent

sealed class SettingsIntent : UiIntent {
    data class OnSettingClick(val setting: SettingModel) : SettingsIntent()
}
