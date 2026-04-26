package com.wojdor.memolki.ui.feature.menu

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.domain.model.MenuModel
import com.wojdor.memolki.domain.usecase.CheckDailyLoginStreakUseCase
import com.wojdor.memolki.domain.usecase.GetMenuUseCase
import com.wojdor.memolki.domain.usecase.GetMoreAppsUseCase
import com.wojdor.memolki.domain.usecase.GetTotalCardPairsMatchedUseCase
import com.wojdor.memolki.domain.usecase.GetTotalCoinsUseCase
import com.wojdor.memolki.domain.usecase.GetTotalGamesPlayedUseCase
import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.util.provider.PackageNameProvider
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenChooseBoardScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenCollectionScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenLeaderboardScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenMoreAppsScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenSettingsScreen
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnCollectionClick
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnDailyRewardClick
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnLeaderboardClick
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnMoreAppsClick
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnPlayClick
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnSettingsClick
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.media.HapticFeedback
import com.wojdor.memolki.util.provider.RecordingModeProvider.RECORDING_MODE
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MenuViewModel(
    savedStateHandle: SavedStateHandle,
    private val analytics: Analytics,
    private val hapticFeedback: HapticFeedback,
    private val getMenuUseCase: GetMenuUseCase,
    private val getMoreAppsUseCase: GetMoreAppsUseCase,
    private val getTotalCoinsUseCase: GetTotalCoinsUseCase,
    private val getTotalCardPairsMatchedUseCase: GetTotalCardPairsMatchedUseCase,
    private val getTotalGamesPlayedUseCase: GetTotalGamesPlayedUseCase,
    private val checkDailyLoginStreakUseCase: CheckDailyLoginStreakUseCase,
    private val packageNameProvider: PackageNameProvider
) : MviViewModel<MenuIntent, MenuState>(
    savedStateHandle,
    MenuState.serializer(),
    MenuState()
) {

    init {
        sendState { copy(currentApp = resolveCurrentApp()) }
        loadMenu()
    }

    private fun resolveCurrentApp(): AppModel? {
        val packageName = packageNameProvider.providePackageName()
        return AppModel.all().firstOrNull { it.appId == packageName }
    }

    override fun onIntent(intent: MenuIntent) {
        when (intent) {
            OnPlayClick -> onPlayClick()
            OnCollectionClick -> onCollectionClick()
            OnLeaderboardClick -> onLeaderboardClick()
            OnSettingsClick -> onSettingsClick()
            OnMoreAppsClick -> onMoreAppsClick()
            OnDailyRewardClick -> onDailyRewardClick()
            MenuIntent.OnScreenResume -> refreshDailyReward()
        }
    }

    private fun onPlayClick() {
        hapticFeedback.vibrateLow()
        sendEffect(OpenChooseBoardScreen)
    }

    private fun onDailyRewardClick() {
        hapticFeedback.vibrateLow()
        analytics.logShopOpenedFromDailyReward()
        sendEffect(MenuEffect.OpenShopScreen)
    }

    private fun onCollectionClick() {
        hapticFeedback.vibrateLow()
        sendEffect(OpenCollectionScreen)
    }

    private fun onLeaderboardClick() {
        hapticFeedback.vibrateLow()
        analytics.logLeaderboardOpened()
        sendEffect(OpenLeaderboardScreen)
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

    private fun refreshDailyReward() {
        viewModelScope.launch {
            val totalGamesPlayed = getTotalGamesPlayedUseCase().first().getOrDefault(0)
            val streakResult = checkDailyLoginStreakUseCase().first()
            val isDailyRewardAvailable = !RECORDING_MODE && totalGamesPlayed > 0 &&
                    streakResult.getOrNull()?.isRewardAvailable == true
            sendState {
                val baseMenu = menu.filterNot { it is MenuModel.DailyReward }
                val menuItems = baseMenu.toMutableList().apply {
                    if (isDailyRewardAvailable) {
                        val insertIndex = indexOfFirst { it is MenuModel.Collection }
                            .takeIf { it >= 0 }?.let { it + 1 } ?: size
                        add(insertIndex, MenuModel.DailyReward)
                    }
                }
                copy(menu = menuItems)
            }
        }
    }

    private fun loadMenu() {
        combine(
            getMenuUseCase(),
            getMoreAppsUseCase(),
            getTotalGamesPlayedUseCase(),
            checkDailyLoginStreakUseCase()
        ) { menuResult, moreAppsResult, totalGamesPlayedResult, streakResult ->
            val totalGamesPlayed = totalGamesPlayedResult.getOrDefault(0)
            val currentApp = uiState.value.otherAppModel
            val randomApp = if (currentApp != null) {
                currentApp
            } else if (totalGamesPlayed >= MINIMUM_GAMES_PLAYED) {
                moreAppsResult.getOrDefault(emptyList()).randomOrNull()
            } else {
                null
            }
            val isDailyRewardAvailable = !RECORDING_MODE && totalGamesPlayed > 0 &&
                    streakResult.getOrNull()?.isRewardAvailable == true
            val baseMenu = menuResult.getOrNull() ?: uiState.value.menu
            val menuItems = baseMenu.toMutableList().apply {
                if (isDailyRewardAvailable) {
                    val insertIndex = indexOfFirst { it is MenuModel.Collection }
                        .takeIf { it >= 0 }?.let { it + 1 } ?: size
                    add(insertIndex, MenuModel.DailyReward)
                }
            }
            Triple(menuItems, randomApp, totalGamesPlayed > 0)
        }.onEach { (menuItems, randomApp, hasPlayedAnyGame) ->
            sendState {
                copy(
                    menu = menuItems,
                    otherAppModel = randomApp ?: otherAppModel,
                    hasPlayedAnyGame = hasPlayedAnyGame
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun sendLeaderboardScores() {
        viewModelScope.launch {
            getTotalCoinsUseCase().first().onSuccess { totalCoins ->
                sendEffect(MenuEffect.SendTotalCoinsScore(totalCoins))
            }
            getTotalCardPairsMatchedUseCase().first().onSuccess { totalCardPairsMatched ->
                sendEffect(MenuEffect.SendTotalCardPairsMatchedScore(totalCardPairsMatched))
            }
        }
    }

    companion object {
        private const val MINIMUM_GAMES_PLAYED = 3
    }
}
