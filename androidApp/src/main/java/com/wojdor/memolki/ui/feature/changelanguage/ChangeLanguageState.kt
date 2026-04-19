package com.wojdor.memolki.ui.feature.changelanguage

import com.wojdor.memolki.domain.model.LanguageModel
import com.wojdor.memolki.ui.base.UiState
import kotlinx.serialization.Serializable

@Serializable
data class ChangeLanguageState(
    val languages: List<LanguageModel> = emptyList(),
    val currentLanguage: LanguageModel = LanguageModel(),
    val isLanguageChangeInProgress: Boolean = false
) : UiState
