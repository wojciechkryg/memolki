package com.wojdor.memolki.ui.feature.changelanguage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.usecase.ChangeLanguageUseCase
import com.wojdor.memolki.domain.usecase.GetLanguagesWithCurrentUseCase
import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.util.media.HapticFeedback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ChangeLanguageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val hapticFeedback: HapticFeedback,
    private val getLanguagesWithCurrentUseCase: GetLanguagesWithCurrentUseCase,
    private val changeLanguageUseCase: ChangeLanguageUseCase
) : MviViewModel<ChangeLanguageIntent, ChangeLanguageState>(
    savedStateHandle,
    ChangeLanguageState()
) {

    private var pendingLanguageTag: String? = null

    init {
        loadLanguages()
    }

    override fun onIntent(intent: ChangeLanguageIntent) {
        when (intent) {
            is ChangeLanguageIntent.OnLanguageClick -> onLanguageClick(intent.language.tag)
            is ChangeLanguageIntent.OnLanguageChangeReady -> changeLanguage()
        }
    }

    private fun onLanguageClick(tag: String) {
        if (uiState.value.isLanguageChangeInProgress) return
        hapticFeedback.vibrateLow()
        if (tag == uiState.value.currentLanguage.tag) return
        pendingLanguageTag = tag
        sendState { copy(isLanguageChangeInProgress = true) }
    }

    private fun changeLanguage() {
        val tag = pendingLanguageTag ?: return
        changeLanguageUseCase(tag).launchIn(viewModelScope)
    }

    private fun loadLanguages() {
        getLanguagesWithCurrentUseCase().onEach {
            it.onSuccess { (languages, currentLanguage) ->
                sendState { copy(languages = languages, currentLanguage = currentLanguage) }
            }
        }.launchIn(viewModelScope)
    }
}
