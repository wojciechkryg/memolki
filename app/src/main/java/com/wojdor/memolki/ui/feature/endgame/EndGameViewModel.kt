package com.wojdor.memolki.ui.feature.endgame

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.model.EndGameMenuModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.GetCoinsUseCase
import com.wojdor.memolki.domain.usecase.IncrementTotalGamesPlayedUseCase
import com.wojdor.memolki.domain.usecase.RewardCoinsForLevelUseCase
import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.util.media.HapticFeedback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import javax.inject.Inject

@HiltViewModel
class EndGameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val hapticFeedback: HapticFeedback,
    private val incrementTotalGamesPlayedUseCase: IncrementTotalGamesPlayedUseCase,
    private val getCoinsUseCase: GetCoinsUseCase,
    private val rewardCoinsForLevelUseCase: RewardCoinsForLevelUseCase
) : MviViewModel<EndGameIntent, EndGameState>(
    savedStateHandle,
    EndGameState()
) {

    override fun onIntent(intent: EndGameIntent) {
        when (intent) {
            is EndGameIntent.OnEndGameShow -> onEndGameShow(level = intent.levelModel)
            is EndGameIntent.OnPlayAgainClick -> onPlayAgainClick(intent)
            is EndGameIntent.OnMenuClick -> onMenuClick()
        }
    }

    private fun onEndGameShow(level: LevelModel) {
        sendState { EndGameState() }
        incrementTotalGamesPlayedUseCase().launchIn(viewModelScope)
        getCurrentCoinsAndReward(level)
    }

    private fun onPlayAgainClick(intent: EndGameIntent.OnPlayAgainClick) {
        hapticFeedback.vibrateLow()
        sendEffect(EndGameEffect.OpenGameScreen(intent.levelModel))
    }

    private fun onMenuClick() {
        hapticFeedback.vibrateLow()
        sendEffect(EndGameEffect.OpenMenuScreen)
    }

    private fun getCurrentCoinsAndReward(level: LevelModel) {
        getCoinsUseCase().take(1).onEach {
            it.onSuccess { currentCoins ->
                sendState {
                    copy(
                        level = level,
                        currentCoins = currentCoins,
                        animateCoins = false,
                        menu = listOf(EndGameMenuModel.PlayAgain, EndGameMenuModel.Menu),
                    )
                }
                rewardCoins(level, currentCoins)
            }
        }.launchIn(viewModelScope)
    }

    private fun rewardCoins(level: LevelModel, currentCoins: Long) {
        rewardCoinsForLevelUseCase(level).take(1).onEach {
            it.onSuccess { rewardedCoins ->
                sendState {
                    copy(
                        rewardedCoins = rewardedCoins,
                        currentCoins = currentCoins + rewardedCoins,
                        animateCoins = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}
