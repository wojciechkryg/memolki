package com.wojdor.memolki.ui.feature.menu

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.usecase.GetMenuUseCase
import com.wojdor.memolki.games.GooglePlayGames
import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenChooseLevelScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenCollectionScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenLeaderboardScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenSettingsScreen
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnCollectionClick
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnLeaderboardClick
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnNewGameClick
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnSettingsClick
import com.wojdor.memolki.util.media.HapticFeedback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val hapticFeedback: HapticFeedback,
    private val googlePlayGames: GooglePlayGames,
    private val getMenuUseCase: GetMenuUseCase
) : MviViewModel<MenuIntent, MenuState>(
    savedStateHandle,
    MenuState()
) {

    init {
        loadMenu()
    }

    override fun onIntent(intent: MenuIntent) {
        when (intent) {
            OnNewGameClick -> onNewGameClick()
            OnCollectionClick -> onCollectionClick()
            OnLeaderboardClick -> onLeaderboardClick()
            OnSettingsClick -> onSettingsClick()
        }
    }

    private fun onNewGameClick() {
        hapticFeedback.vibrateLow()
        sendEffect(OpenChooseLevelScreen)
    }

    private fun onCollectionClick() {
        hapticFeedback.vibrateLow()
        sendEffect(OpenCollectionScreen)
    }

    private fun onLeaderboardClick() {
        hapticFeedback.vibrateLow()
        sendEffect(OpenLeaderboardScreen(googlePlayGames))
    }

    private fun onSettingsClick() {
        hapticFeedback.vibrateLow()
        sendEffect(OpenSettingsScreen)
    }

    private fun loadMenu() {
        getMenuUseCase().onEach {
            it.onSuccess { menu ->
                sendState { copy(menu = menu) }
            }
        }.launchIn(viewModelScope)
    }
}
