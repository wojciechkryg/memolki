package com.wojdor.memolki.ui.feature.settings

import com.wojdor.memolki.domain.model.SettingModel

data class SettingsCallbacks(
    val onSettingToggle: (SettingModel) -> Unit = { }
)
