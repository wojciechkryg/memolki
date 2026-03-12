package com.wojdor.memolki.ui.feature.changelanguage

import com.wojdor.memolki.domain.model.LanguageModel

data class ChangeLanguageCallbacks(
    val onLanguageChange: (LanguageModel) -> Unit = { },
    val onLanguageChangeReady: () -> Unit = { }
)
