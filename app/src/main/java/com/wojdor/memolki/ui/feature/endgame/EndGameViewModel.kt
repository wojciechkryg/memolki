package com.wojdor.memolki.ui.feature.endgame

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.review.ReviewManager
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.model.EndGameMenuModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.GetCoinsUseCase
import com.wojdor.memolki.domain.usecase.GetTotalCoinsUseCase
import com.wojdor.memolki.domain.usecase.IncrementTotalGamesPlayedUseCase
import com.wojdor.memolki.domain.usecase.RewardCoinsForLevelUseCase
import com.wojdor.memolki.games.GooglePlayGames
import com.wojdor.memolki.ui.ads.AllRewardedAds
import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.ui.feature.endgame.EndGameEffect.SendTotalCoinsScore
import com.wojdor.memolki.util.media.CoinsPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import com.wojdor.memolki.util.media.LevelCompletePlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EndGameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val levelCompletePlayer: LevelCompletePlayer,
    private val coinsPlayer: CoinsPlayer,
    private val hapticFeedback: HapticFeedback,
    private val allRewardedAds: AllRewardedAds,
    private val reviewManager: ReviewManager,
    private val googlePlayGames: GooglePlayGames,
    private val userRepository: UserRepository,
    private val incrementTotalGamesPlayedUseCase: IncrementTotalGamesPlayedUseCase,
    private val getCoinsUseCase: GetCoinsUseCase,
    private val rewardCoinsForLevelUseCase: RewardCoinsForLevelUseCase,
    private val getTotalCoinsUseCase: GetTotalCoinsUseCase
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
            EndGameIntent.OnAdReward -> onAdReward()
            is EndGameIntent.OnAdDismiss -> onAdDismiss(intent.wasRewardGranted)
        }
    }

    private fun onEndGameShow(level: LevelModel) {
        sendState { EndGameState() }
        loadAd()
        incrementTotalGamesPlayedUseCase().launchIn(viewModelScope)
        getCurrentCoinsAndReward(level)
        viewModelScope.launch {
            delay(LEVEL_COMPLETE_SOUND_DELAY)
            levelCompletePlayer.play()
            requestReview()
        }
    }

    private suspend fun requestReview() {
        val totalGamesPlayed = userRepository.getTotalGamesPlayed().first()
        if (totalGamesPlayed >= MIN_GAMES_PLAYED_TO_ASK_REVIEW) {
            val request = reviewManager.requestReviewFlow()
            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    sendEffect(EndGameEffect.RequestReview(reviewManager, request.result))
                }
            }
        }
    }

    private fun onAdReward() {
        showMenu(false)
    }

    private fun onAdDismiss(wasRewardGranted: Boolean) {
        if (wasRewardGranted) {
            rewardCoinsForAd()
        }
        loadAd(wasRewardGranted)
    }

    private fun rewardCoinsForAd() {
        getCurrentCoinsAndReward(uiState.value.level, isRewardFromAd = true)
    }

    private fun loadAd(wasRewardGranted: Boolean = false) {
        if (allRewardedAds.endGameCoinsAd.isLoaded && !wasRewardGranted) {
            showMenu(true)
        } else {
            showMenu(false)
            allRewardedAds.endGameCoinsAd.load(
                onLoaded = {
                    if (!wasRewardGranted) {
                        showMenu(true)
                    }
                },
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
        sendEffect(EndGameEffect.ShowAd(allRewardedAds.endGameCoinsAd))
    }

    private fun showMenu(isAdLoaded: Boolean) {
        sendState {
            copy(menu = getBaseMenu().let {
                if (isAdLoaded) listOf(EndGameMenuModel.WatchAd) + it else it
            })
        }
    }

    private fun getCurrentCoinsAndReward(level: LevelModel, isRewardFromAd: Boolean = false) {
        viewModelScope.launch {
            getCoinsUseCase().first().onSuccess { currentCoins ->
                sendState {
                    copy(
                        level = level,
                        currentCoins = currentCoins,
                        animateCoins = false
                    )
                }
                rewardCoins(level, currentCoins, isRewardFromAd)
            }
        }
    }

    private fun rewardCoins(level: LevelModel, currentCoins: Long, isRewardFromAd: Boolean) {
        viewModelScope.launch {
            rewardCoinsForLevelUseCase(level).first().onSuccess { rewardedCoins ->
                sendState {
                    copy(
                        rewardedCoins = if (isRewardFromAd) uiState.value.rewardedCoins + rewardedCoins else rewardedCoins,
                        currentCoins = currentCoins + rewardedCoins,
                        animateCoins = true,
                        animateRewardCoins = isRewardFromAd
                    )
                }
                sendTotalCoinsScore()
                delay(COINS_SOUND_DELAY)
                coinsPlayer.play()
            }
        }
    }

    private fun sendTotalCoinsScore() {
        viewModelScope.launch {
            getTotalCoinsUseCase().first().onSuccess { totalCoins ->
                sendEffect(SendTotalCoinsScore(googlePlayGames, totalCoins))
            }
        }
    }

    private fun getBaseMenu() = listOf(
        EndGameMenuModel.PlayAgain,
        EndGameMenuModel.Menu
    )

    companion object {
        const val LEVEL_COMPLETE_SOUND_DELAY = 250L
        const val COINS_SOUND_DELAY = 500L
        const val MIN_GAMES_PLAYED_TO_ASK_REVIEW = 3
    }
}
