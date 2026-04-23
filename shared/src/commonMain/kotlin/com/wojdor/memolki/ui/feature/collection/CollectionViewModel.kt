package com.wojdor.memolki.ui.feature.collection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.domain.model.CollectionCardPairModel
import com.wojdor.memolki.domain.usecase.GetCoinsUseCase
import com.wojdor.memolki.domain.usecase.GetCollectionDataUseCase
import com.wojdor.memolki.domain.usecase.IncrementUnlockedCardPairsFromAdsCountUseCase
import com.wojdor.memolki.domain.usecase.UnlockRandomCardIfEnoughCoinsUseCase
import com.wojdor.memolki.domain.usecase.UnlockRandomCardUseCase
import com.wojdor.memolki.ui.ads.AllRewardedAds
import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.media.CoinsPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import com.wojdor.memolki.util.notification.NotificationScheduler
import com.wojdor.memolki.util.provider.RecordingModeProvider.RECORDING_MODE
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class CollectionViewModel(
    savedStateHandle: SavedStateHandle,
    private val analytics: Analytics,
    private val coinsPlayer: CoinsPlayer,
    private val hapticFeedback: HapticFeedback,
    private val allRewardedAds: AllRewardedAds,
    private val getCoinsUseCase: GetCoinsUseCase,
    private val getCollectionDataUseCase: GetCollectionDataUseCase,
    private val unlockRandomCardIfEnoughCoinsUseCase: UnlockRandomCardIfEnoughCoinsUseCase,
    private val unlockRandomCardUseCase: UnlockRandomCardUseCase,
    private val incrementUnlockedCardPairsFromAdsCountUseCase: IncrementUnlockedCardPairsFromAdsCountUseCase,
    private val notificationScheduler: NotificationScheduler
) : MviViewModel<CollectionIntent, CollectionState>(
    savedStateHandle,
    CollectionState.serializer(),
    CollectionState()
) {

    private var loadCoinsJob: Job? = null
    private var hasLoggedCollectionView = false

    init {
        loadData()
    }

    override fun onIntent(intent: CollectionIntent) {
        when (intent) {
            CollectionIntent.OnShopClick -> onShopClick()
            is CollectionIntent.OnCardPairClick -> onCardPairClick(intent)
            is CollectionIntent.OnUnlockWithCoinsClick -> onUnlockWithCoinsClick()
            is CollectionIntent.OnUnlockWithAdClick -> onUnlockWithAdClick()
            CollectionIntent.OnAdReward -> onAdReward()
            is CollectionIntent.OnAdDismiss -> onAdDismiss(intent.wasRewardGranted)
        }
    }

    private fun onAdReward() {
        loadCardPairs(isAdAvailable = false)
        incrementUnlockedCardPairsFromAdsCountUseCase().launchIn(viewModelScope)
    }

    private fun onAdDismiss(wasRewardGranted: Boolean) {
        analytics.logAdDismissed(PLACEMENT, wasRewardGranted)
        if (wasRewardGranted) {
            rewardCardForAd()
            checkShouldShowNotificationRequest()
        }
        loadCardPairsAndAd(wasRewardGranted)
    }

    private fun checkShouldShowNotificationRequest() {
        if (!notificationScheduler.hasNotificationPermission()) {
            sendEffect(CollectionEffect.OpenEnableNotificationsScreen)
        }
    }

    private fun rewardCardForAd() {
        unlockRandomCardUseCase().onEach { result ->
            result.onSuccess {
                val unlockedCount = uiState.value.collectionCardPairs
                    .count { it is CollectionCardPairModel.Unlocked } + 1
                val totalCount = uiState.value.collectionCardPairs.size
                analytics.logCardUnlockedWithAd(unlockedCount, totalCount)
                analytics.logAdRewardFromCollection()
                loadData()
            }
        }.launchIn(viewModelScope)
    }

    private fun loadCardPairsAndAd(wasRewardGranted: Boolean = false) {
        allRewardedAds.collectionCardPairAd.loadAndNotify(wasRewardGranted) { isAvailable ->
            loadCardPairs(isAdAvailable = isAvailable)
        }
    }

    private fun loadData(animateCoins: Boolean = false) {
        loadCardPairsAndAd()
        loadCoins(animateCoins)
    }

    private fun loadCoins(animateCoins: Boolean) {
        loadCoinsJob?.cancel()
        loadCoinsJob = getCoinsUseCase().onEach {
            it.onSuccess { coins ->
                sendState { copy(coins = coins, animateCoins = animateCoins) }
            }
        }.launchIn(viewModelScope)
    }

    private fun loadCardPairs(isAdAvailable: Boolean) {
        getCollectionDataUseCase().onEach { result ->
            result.onSuccess { data ->
                val lockedCardPairsCount = data.allCardPairsCount - data.unlockedCardPairs.size
                val collectionCardPairs = getCollectionCardPairs(
                    data.unlockedCardPairs,
                    lockedCardPairsCount,
                    data.nextCardPairCost,
                    data.unlockedCardPairsFromAdsCount,
                    isAdAvailable
                )
                if (!hasLoggedCollectionView) {
                    hasLoggedCollectionView = true
                    val unlockedCount =
                        collectionCardPairs.count { it is CollectionCardPairModel.Unlocked }
                    analytics.logCollectionViewed(unlockedCount, collectionCardPairs.size)
                }
                sendState { copy(collectionCardPairs = collectionCardPairs) }
            }
        }.launchIn(viewModelScope)
    }

    private fun getCollectionCardPairs(
        unlockedCardPairs: List<CardPairModel>,
        lockedCardPairsCount: Int,
        cardPairCost: Int,
        unlockedCardPairsFromAdsCount: Int,
        isAdAvailable: Boolean
    ): List<CollectionCardPairModel> {
        val unlocked = unlockedCardPairs.map { CollectionCardPairModel.Unlocked(it) }
        val lockedToUnlockWithCoins = getLockedToUnlockWithCoins(lockedCardPairsCount, cardPairCost)
        val lockedToUnlockWithAd =
            getLockedToUnlockWithAd(
                lockedCardPairsCount,
                unlockedCardPairsFromAdsCount,
                isAdAvailable
            )
        val lockedCardPairsNotPossibleToUnlockYet =
            getLockedNotPossibleToUnlockYet(lockedCardPairsCount)
        return unlocked +
                lockedToUnlockWithCoins +
                lockedToUnlockWithAd +
                lockedCardPairsNotPossibleToUnlockYet
    }

    private fun getLockedToUnlockWithCoins(
        lockedCardPairsCount: Int,
        cardPairCost: Int
    ): List<CollectionCardPairModel.LockedToUnlockWithCoins> =
        if (lockedCardPairsCount > NO_LOCKED_CARD_PAIRS) {
            List(UNLOCK_WITH_COINS_COUNT) {
                CollectionCardPairModel.LockedToUnlockWithCoins(cardPairCost)
            }
        } else {
            emptyList()
        }

    private fun getLockedToUnlockWithAd(
        lockedCardPairsCount: Int,
        unlockedCardPairsFromAdsCount: Int,
        isAdAvailable: Boolean
    ): List<CollectionCardPairModel> {
        return if (lockedCardPairsCount > LAST_LOCKED_CARD_PAIR) {
            if (RECORDING_MODE) return List(UNLOCK_WITH_ADS_COUNT) { CollectionCardPairModel.Locked }
            if (unlockedCardPairsFromAdsCount < MAX_UNLOCKED_CARD_PAIRS_WITH_ADS && isAdAvailable) {
                List(UNLOCK_WITH_ADS_COUNT) { CollectionCardPairModel.LockedToUnlockWithAd }
            } else {
                List(UNLOCK_WITH_ADS_COUNT) { CollectionCardPairModel.Locked }
            }
        } else {
            emptyList()
        }
    }

    private fun getLockedNotPossibleToUnlockYet(lockedCardPairsCount: Int)
            : List<CollectionCardPairModel.Locked> {
        val lockedCardPairsNotPossibleToUnlockYet =
            (lockedCardPairsCount - NUMBER_OF_LOCKED_CARDS_POSSIBLE_TO_UNLOCK).coerceAtLeast(0)
        return List(lockedCardPairsNotPossibleToUnlockYet) { CollectionCardPairModel.Locked }
    }

    private fun onShopClick() {
        hapticFeedback.vibrateLow()
        analytics.logShopOpenedFromCollection()
        sendEffect(CollectionEffect.OpenShopScreen)
    }

    private fun onCardPairClick(intent: CollectionIntent.OnCardPairClick) {
        hapticFeedback.vibrateLow()
        analytics.logCardPairDetailsViewed()
        sendEffect(
            CollectionEffect.OpenCardPairDetailsScreen(
                intent.collectionCardPairModel.cardPair
            )
        )
    }

    private fun onUnlockWithCoinsClick() {
        hapticFeedback.vibrateLow()
        unlockRandomCardIfEnoughCoinsUseCase().onEach { result ->
            result.onSuccess {
                val unlockedCount = uiState.value.collectionCardPairs
                    .count { it is CollectionCardPairModel.Unlocked } + 1
                val totalCount = uiState.value.collectionCardPairs.size
                analytics.logCardUnlockedWithCoins(unlockedCount, totalCount)
                coinsPlayer.playDelayed()
                loadData(animateCoins = true)
            }.onFailure {
                val cardCost = uiState.value.collectionCardPairs
                    .filterIsInstance<CollectionCardPairModel.LockedToUnlockWithCoins>()
                    .firstOrNull()?.coins ?: 0
                analytics.logInsufficientCoinsShown(uiState.value.coins, cardCost)
                analytics.logShopOpenedFromInsufficientCoins()
                sendEffect(CollectionEffect.OpenShopScreen)
            }
        }.launchIn(viewModelScope)
    }

    private fun onUnlockWithAdClick() {
        hapticFeedback.vibrateLow()
        analytics.logAdShown(PLACEMENT)
        sendEffect(CollectionEffect.ShowAd(allRewardedAds.collectionCardPairAd))
    }

    companion object {
        const val NO_LOCKED_CARD_PAIRS = 0
        const val LAST_LOCKED_CARD_PAIR = 1
        const val UNLOCK_WITH_ADS_COUNT = 1
        const val UNLOCK_WITH_COINS_COUNT = 1
        const val MAX_UNLOCKED_CARD_PAIRS_WITH_ADS = 3

        const val NUMBER_OF_LOCKED_CARDS_POSSIBLE_TO_UNLOCK =
            UNLOCK_WITH_COINS_COUNT + UNLOCK_WITH_ADS_COUNT

        private const val PLACEMENT = "collection"
    }
}
