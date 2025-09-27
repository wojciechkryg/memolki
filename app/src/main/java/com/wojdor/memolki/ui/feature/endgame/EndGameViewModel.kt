package com.wojdor.memolki.ui.feature.endgame

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.model.EndGameMenuModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.GetCoinsUseCase
import com.wojdor.memolki.domain.usecase.IncrementTotalGamesPlayedUseCase
import com.wojdor.memolki.domain.usecase.RewardCoinsForLevelUseCase
import com.wojdor.memolki.ui.ads.AllRewardedAds
import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.util.media.CoinsPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import com.wojdor.memolki.util.media.LevelCompletePlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EndGameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val levelCompletePlayer: LevelCompletePlayer,
    private val coinsPlayer: CoinsPlayer,
    private val hapticFeedback: HapticFeedback,
    private val rewardedAds: AllRewardedAds,
    private val incrementTotalGamesPlayedUseCase: IncrementTotalGamesPlayedUseCase,
    private val getCoinsUseCase: GetCoinsUseCase,
    private val rewardCoinsForLevelUseCase: RewardCoinsForLevelUseCase
) : MviViewModel<EndGameIntent, EndGameState>(
    savedStateHandle,
    EndGameState()
) {

    override fun onIntent(intent: EndGameIntent) {
        when (intent) {
            is EndGameIntent.OnEndGameShow -> onEndGameShow(intent.levelModel)
            is EndGameIntent.OnPlayAgainClick -> onPlayAgainClick(intent)
            EndGameIntent.OnMenuClick -> onMenuClick()
            EndGameIntent.OnWatchAdClick -> onWatchAdClick()
            EndGameIntent.OnAdReward -> rewardCoinsForAd()
            EndGameIntent.OnAdDismiss -> loadAds()
        }
    }

    private fun onEndGameShow(level: LevelModel) {
        sendState { EndGameState() }
        loadAds()
        incrementTotalGamesPlayedUseCase().launchIn(viewModelScope)
        getCurrentCoinsAndReward(level)
        viewModelScope.launch {
            delay(250)
            levelCompletePlayer.play()
        }
    }

    private fun loadAds() {
        if (rewardedAds.endGameCoinsAd.isLoaded) {
            showMenu(true)
        } else {
            showMenu(false)
            rewardedAds.endGameCoinsAd.load(
                onLoaded = { showMenu(true) },
                onFailed = { showMenu(false) }
            )
        }
    }

    private fun onPlayAgainClick(intent: EndGameIntent.OnPlayAgainClick) {
        hapticFeedback.vibrateLow()
        sendEffect(EndGameEffect.OpenGameScreen(intent.levelModel))
    }

    private fun onMenuClick() {
        hapticFeedback.vibrateLow()
        sendEffect(EndGameEffect.OpenMenuScreen)
    }

    private fun onWatchAdClick() {
        hapticFeedback.vibrateLow()
        sendEffect(EndGameEffect.ShowAd(rewardedAds.endGameCoinsAd))
    }

    private fun showMenu(isAdLoaded: Boolean) {
        sendState {
            copy(menu = getBaseMenu().let {
                if (isAdLoaded) listOf(EndGameMenuModel.WatchAd) + it else it
            })
        }
    }

    private fun getCurrentCoinsAndReward(level: LevelModel) {
        getCoinsUseCase().take(1).onEach {
            it.onSuccess { currentCoins ->
                sendState {
                    copy(
                        level = level,
                        currentCoins = currentCoins,
                        animateCoins = false
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
                delay(COINS_SOUND_DELAY)
                coinsPlayer.play()
            }
        }.launchIn(viewModelScope)
    }

    private fun rewardCoinsForAd() {
        showMenu(false)
        rewardedAds.endGameCoinsAd.load()
        viewModelScope.launch {
            // TODO: Reward for watching ad
            delay(COINS_SOUND_DELAY)
            coinsPlayer.play()
        }
    }

    private fun getBaseMenu() = listOf(
        EndGameMenuModel.PlayAgain,
        EndGameMenuModel.Menu
    )

    companion object {
        const val COINS_SOUND_DELAY = 500L
    }
}
