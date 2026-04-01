package com.wojdor.memolki.ui.feature.endgame

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.review.ReviewManager
import com.wojdor.memolki.domain.model.EndGameMenuModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.CanUnlockNewCardUseCase
import com.wojdor.memolki.domain.usecase.CheckDailyLoginStreakUseCase
import com.wojdor.memolki.domain.usecase.GetCoinsUseCase
import com.wojdor.memolki.domain.usecase.GetTotalCoinsUseCase
import com.wojdor.memolki.domain.usecase.GetTotalGamesPlayedUseCase
import com.wojdor.memolki.domain.usecase.HasReceivedShareRewardUseCase
import com.wojdor.memolki.domain.usecase.IncrementTotalGamesPlayedUseCase
import com.wojdor.memolki.domain.usecase.RewardCoinsForLevelUseCase
import com.wojdor.memolki.domain.usecase.RewardCoinsForShareUseCase
import com.wojdor.memolki.domain.usecase.ShouldShowNotificationRequestUseCase
import com.wojdor.memolki.ui.ads.AllRewardedAds
import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.ui.feature.enablenotifications.EnableNotificationDestination
import com.wojdor.memolki.ui.feature.endgame.EndGameEffect.SendTotalCoinsScore
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.extension.logE
import com.wojdor.memolki.util.media.CoinsPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import com.wojdor.memolki.util.media.LevelCompletePlayer
import com.wojdor.memolki.util.playgames.GooglePlayGames
import com.wojdor.memolki.util.provider.RecordingModeProvider.RECORDING_MODE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EndGameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val analytics: Analytics,
    private val levelCompletePlayer: LevelCompletePlayer,
    private val coinsPlayer: CoinsPlayer,
    private val hapticFeedback: HapticFeedback,
    private val allRewardedAds: AllRewardedAds,
    private val reviewManager: ReviewManager,
    private val googlePlayGames: GooglePlayGames,
    private val incrementTotalGamesPlayedUseCase: IncrementTotalGamesPlayedUseCase,
    private val getTotalGamesPlayedUseCase: GetTotalGamesPlayedUseCase,
    private val getCoinsUseCase: GetCoinsUseCase,
    private val rewardCoinsForLevelUseCase: RewardCoinsForLevelUseCase,
    private val getTotalCoinsUseCase: GetTotalCoinsUseCase,
    private val canUnlockNewCardUseCase: CanUnlockNewCardUseCase,
    private val shouldShowNotificationRequestUseCase: ShouldShowNotificationRequestUseCase,
    private val rewardCoinsForShareUseCase: RewardCoinsForShareUseCase,
    private val hasReceivedShareRewardUseCase: HasReceivedShareRewardUseCase,
    private val checkDailyLoginStreakUseCase: CheckDailyLoginStreakUseCase
) : MviViewModel<EndGameIntent, EndGameState>(
    savedStateHandle,
    EndGameState()
) {

    private var isAdLoaded = false
    private var shouldShowNotificationRequest = false
    private var isNotificationRequestDismissed = false
    private var isDailyStreakRewardAvailable = false
    private var isShareRewardAvailable = false

    override fun onIntent(intent: EndGameIntent) {
        when (intent) {
            is EndGameIntent.OnEndGameShow -> onEndGameShow(intent.levelModel)
            is EndGameIntent.OnPlayAgainClick -> onPlayAgainClick(intent)
            EndGameIntent.OnMenuClick -> onMenuClick()
            EndGameIntent.OnUnlockNewCardClick -> onUnlockNewCardClick()
            EndGameIntent.OnWatchAdClick -> onWatchAdClick()
            EndGameIntent.OnAdReward -> onAdReward()
            is EndGameIntent.OnAdDismiss -> onAdDismiss(intent.wasRewardGranted)
            EndGameIntent.OnShareClick -> onShareClick()
            EndGameIntent.OnFreeCoinsClick -> onFreeCoinsClick()
            EndGameIntent.OnScreenResume -> onScreenResume()
        }
    }

    private fun onShareClick() {
        hapticFeedback.vibrateLow()
        analytics.logShareClicked(isShareRewardAvailable)
        rewardCoinsForShareUseCase().onEach { result ->
            result.onSuccess { wasRewarded ->
                if (wasRewarded) {
                    isShareRewardAvailable = false
                    coinsPlayer.play()
                    sendState { copy(animateCoins = true) }
                    reloadCoins()
                    showMenu()
                }
            }.onFailure {
                logE("Failed to reward share coins", it)
            }
        }.launchIn(viewModelScope)
        sendEffect(EndGameEffect.Share)
    }

    private fun checkShareRewardAvailable() {
        hasReceivedShareRewardUseCase().onEach { result ->
            result.onSuccess { hasReceived ->
                isShareRewardAvailable = !hasReceived
                showMenu()
            }.onFailure {
                isShareRewardAvailable = false
                showMenu()
            }
        }.launchIn(viewModelScope)
    }

    private fun onScreenResume() {
        checkShareRewardAvailable()
        checkDailyStreakRewardAvailable()
        reloadCoins()
    }

    private fun onFreeCoinsClick() {
        hapticFeedback.vibrateLow()
        analytics.logShopOpenedFromEndGame()
        shouldShowNotificationRequest = false
        isNotificationRequestDismissed = true
        sendEffect(EndGameEffect.OpenShopScreen)
    }

    private fun checkDailyStreakRewardAvailable() {
        checkDailyLoginStreakUseCase().onEach { result ->
            result.onSuccess { streakResult ->
                isDailyStreakRewardAvailable = streakResult.isRewardAvailable
                showMenu()
            }.onFailure {
                isDailyStreakRewardAvailable = false
                showMenu()
            }
        }.launchIn(viewModelScope)
    }

    private fun reloadCoins() {
        viewModelScope.launch {
            getCoinsUseCase().first().onSuccess { coins ->
                sendState { copy(currentCoins = coins) }
            }
        }
    }

    private fun onEndGameShow(level: LevelModel) {
        sendState { EndGameState(showSparkles = true) }
        loadAd()
        incrementTotalGamesPlayedUseCase().launchIn(viewModelScope)
        checkShouldShowNotificationRequest()
        checkShareRewardAvailable()
        checkDailyStreakRewardAvailable()
        getCurrentCoinsAndReward(level)
        viewModelScope.launch {
            delay(LEVEL_COMPLETE_SOUND_DELAY)
            levelCompletePlayer.play()
            requestReview()
        }
    }

    private fun checkShouldShowNotificationRequest() {
        shouldShowNotificationRequestUseCase().onEach { result ->
            result.onSuccess {
                if (!isNotificationRequestDismissed) {
                    shouldShowNotificationRequest = it
                }
            }.onFailure {
                shouldShowNotificationRequest = false
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun requestReview() {
        val totalGamesPlayed = getTotalGamesPlayedUseCase().first().getOrDefault(0L)
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
        analytics.logAdDismissed(PLACEMENT, wasRewardGranted)
        if (wasRewardGranted) {
            rewardCoinsForAd()
        }
        loadAd(wasRewardGranted)
    }

    private fun rewardCoinsForAd() {
        analytics.logAdRewardFromEndGame()
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
        navigateOrShowNotificationRequest(
            destination = EnableNotificationDestination.GAME,
            defaultEffect = EndGameEffect.OpenGameScreen(intent.levelModel),
            levelModel = intent.levelModel
        )
    }

    private fun onMenuClick() {
        hapticFeedback.vibrateLow()
        navigateOrShowNotificationRequest(
            destination = EnableNotificationDestination.MENU,
            defaultEffect = EndGameEffect.OpenMenuScreen
        )
    }

    private fun onUnlockNewCardClick() {
        hapticFeedback.vibrateLow()
        navigateOrShowNotificationRequest(
            destination = EnableNotificationDestination.COLLECTION,
            defaultEffect = EndGameEffect.OpenCollectionScreen
        )
    }

    private fun navigateOrShowNotificationRequest(
        destination: EnableNotificationDestination,
        defaultEffect: EndGameEffect,
        levelModel: LevelModel? = null
    ) {
        if (!RECORDING_MODE && shouldShowNotificationRequest) {
            shouldShowNotificationRequest = false
            sendEffect(EndGameEffect.OpenEnableNotificationsScreen(destination, levelModel))
        } else {
            sendEffect(defaultEffect)
        }
    }

    private fun onWatchAdClick() {
        hapticFeedback.vibrateLow()
        analytics.logAdShown(PLACEMENT)
        sendEffect(EndGameEffect.ShowAd(allRewardedAds.endGameCoinsAd))
    }

    private fun showMenu() {
        showMenu(isAdLoaded)
    }

    private fun showMenu(isAdLoaded: Boolean) {
        this.isAdLoaded = isAdLoaded
        canUnlockNewCardUseCase().onEach { result ->
            val canUnlockNewCard = result.getOrDefault(false)
            val menu = mutableListOf(EndGameMenuModel.PlayAgain, EndGameMenuModel.Menu).apply {
                if (isAdLoaded && !RECORDING_MODE) {
                    add(0, EndGameMenuModel.WatchAd)
                }
                if (!RECORDING_MODE && isDailyStreakRewardAvailable) {
                    add(EndGameMenuModel.FreeCoins)
                } else if (canUnlockNewCard) {
                    add(EndGameMenuModel.UnlockNewCard)
                }
                if (!RECORDING_MODE) {
                    add(
                        EndGameMenuModel.Share(
                            showReward = isShareRewardAvailable,
                            rewardCoins = if (isShareRewardAvailable) RewardCoinsForShareUseCase.SHARE_REWARD_COINS else 0L
                        )
                    )
                }
            }
            sendState { copy(menu = menu) }
        }.launchIn(viewModelScope)
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
                        animateRewardCoins = isRewardFromAd
                    )
                }
                delay(REWARD_COINS_DELAY)
                coinsPlayer.play()
                sendState {
                    copy(
                        currentCoins = currentCoins + rewardedCoins,
                        animateCoins = true
                    )
                }
                showMenu()
                sendTotalCoinsScore()
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

    companion object {
        const val LEVEL_COMPLETE_SOUND_DELAY = 250L
        const val REWARD_COINS_DELAY = 500L
        const val MIN_GAMES_PLAYED_TO_ASK_REVIEW = 3
        private const val PLACEMENT = "end_game"
    }
}
