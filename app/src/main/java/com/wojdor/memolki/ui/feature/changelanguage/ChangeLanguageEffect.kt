package com.wojdor.memolki.ui.feature.changelanguage

import com.wojdor.memolki.ui.base.UiEffect

sealed class ChangeLanguageEffect : UiEffect {
    data object NavigateBack : ChangeLanguageEffect()
}
