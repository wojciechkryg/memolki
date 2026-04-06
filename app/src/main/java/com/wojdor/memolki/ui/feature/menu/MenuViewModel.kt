package com.wojdor.memolki.ui.feature.menu

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.domain.usecase.GetMenuUseCase
import com.wojdor.memolki.domain.usecase.GetMoreAppsUseCase
import com.wojdor.memolki.domain.usecase.GetTotalCardPairsMatchedUseCase
import com.wojdor.memolki.domain.usecase.GetTotalCoinsUseCase
import com.wojdor.memolki.domain.usecase.GetTotalGamesPlayedUseCase
import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenChooseBoardScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenCollectionScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenLeaderboardScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenMoreAppsScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenSettingsScreen
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnCollectionClick
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnLeaderboardClick
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnMoreAppsClick
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnNewGameClick
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnSettingsClick
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.media.HapticFeedback
import com.wojdor.memolki.util.playgames.GooglePlayGames
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val analytics: Analytics,
    private val hapticFeedback: HapticFeedback,
    private val googlePlayGames: GooglePlayGames,
    private val getMenuUseCase: GetMenuUseCase,
    private val getMoreAppsUseCase: GetMoreAppsUseCase,
    private val getTotalCoinsUseCase: GetTotalCoinsUseCase,
    private val getTotalCardPairsMatchedUseCase: GetTotalCardPairsMatchedUseCase,
    private val getTotalGamesPlayedUseCase: GetTotalGamesPlayedUseCase
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
            OnMoreAppsClick -> onMoreAppsClick()
        }
    }

    private fun onNewGameClick() {
        hapticFeedback.vibrateLow()
        sendEffect(OpenChooseBoardScreen)
    }

    private fun onCollectionClick() {
        hapticFeedback.vibrateLow()
        sendEffect(OpenCollectionScreen)
    }

    private fun onLeaderboardClick() {
        hapticFeedback.vibrateLow()
        analytics.logLeaderboardOpened()
        sendEffect(OpenLeaderboardScreen(googlePlayGames))
        sendLeaderboardScores()
    }

    private fun onSettingsClick() {
        hapticFeedback.vibrateLow()
        sendEffect(OpenSettingsScreen)
    }

    private fun onMoreAppsClick() {
        hapticFeedback.vibrateLow()
        analytics.logMoreAppsClicked()
        sendEffect(OpenMoreAppsScreen)
    }

    private fun loadMenu() {
        combine(
            getMenuUseCase(),
            getMoreAppsUseCase(),
            getTotalGamesPlayedUseCase()
        ) { menuResult, moreAppsResult, totalGamesPlayedResult ->
            Triple(menuResult, moreAppsResult, totalGamesPlayedResult)
        }.onEach { (menuResult, moreAppsResult, totalGamesPlayedResult) ->
            var randomApp: AppModel? = null
            if (totalGamesPlayedResult.getOrDefault(0) >= MINIMUM_GAMES_PLAYED) {
                randomApp = moreAppsResult.getOrDefault(emptyList()).random()
            }
            sendState {
                copy(
                    menu = menuResult.getOrNull() ?: menu,
                    otherAppModel = randomApp ?: otherAppModel
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun sendLeaderboardScores() {
        viewModelScope.launch {
            getTotalCoinsUseCase().first().onSuccess { totalCoins ->
                sendEffect(MenuEffect.SendTotalCoinsScore(googlePlayGames, totalCoins))
            }
            getTotalCardPairsMatchedUseCase().first().onSuccess { totalCardPairsMatched ->
                sendEffect(
                    MenuEffect.SendTotalCardPairsMatchedScore(
                        googlePlayGames,
                        totalCardPairsMatched
                    )
                )
            }
        }
    }

    companion object {
        private const val MINIMUM_GAMES_PLAYED = 3
    }
}
