package com.wojdor.memolki.ui.feature.settings

import com.wojdor.memolki.domain.model.SettingModel
import com.wojdor.memolki.ui.base.UiState
import kotlinx.serialization.Serializable

@Serializable
data class SettingsState(
    val settings: List<SettingModel> = emptyList()
) : UiState
