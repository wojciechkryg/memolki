package com.wojdor.memolki.ui.feature.changelanguage

import com.wojdor.memolki.domain.model.LanguageModel
import com.wojdor.memolki.ui.base.UiIntent

sealed class ChangeLanguageIntent : UiIntent {
    data class OnLanguageClick(val language: LanguageModel) : ChangeLanguageIntent()
    data object OnLanguageChangeReady : ChangeLanguageIntent()
}
