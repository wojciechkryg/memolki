package com.wojdor.memolki.ui.feature.collection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.domain.model.CollectionCardPairModel
import com.wojdor.memolki.domain.usecase.CalculateNextCardPairCostUseCase
import com.wojdor.memolki.domain.usecase.GetAllCardPairsCountUseCase
import com.wojdor.memolki.domain.usecase.GetCoinsUseCase
import com.wojdor.memolki.domain.usecase.GetUnlockedCardPairsFromAdsCountUseCase
import com.wojdor.memolki.domain.usecase.GetUnlockedCardPairsUseCase
import com.wojdor.memolki.domain.usecase.IncrementUnlockedCardPairsFromAdsCountUseCase
import com.wojdor.memolki.domain.usecase.UnlockRandomCardIfEnoughCoinsUseCase
import com.wojdor.memolki.domain.usecase.UnlockRandomCardUseCase
import com.wojdor.memolki.ui.ads.AllRewardedAds
import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.util.media.CoinsPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val coinsPlayer: CoinsPlayer,
    private val hapticFeedback: HapticFeedback,
    private val allRewardedAds: AllRewardedAds,
    private val getCoinsUseCase: GetCoinsUseCase,
    private val getUnlockedCardPairs: GetUnlockedCardPairsUseCase,
    private val getAllCardPairsCountUseCase: GetAllCardPairsCountUseCase,
    private val calculateNextCardPairCostUseCase: CalculateNextCardPairCostUseCase,
    private val unlockRandomCardIfEnoughCoinsUseCase: UnlockRandomCardIfEnoughCoinsUseCase,
    private val unlockRandomCardUseCase: UnlockRandomCardUseCase,
    private val getUnlockedCardPairsFromAdsCountUseCase: GetUnlockedCardPairsFromAdsCountUseCase,
    private val incrementUnlockedCardPairsFromAdsCountUseCase: IncrementUnlockedCardPairsFromAdsCountUseCase
) : MviViewModel<CollectionIntent, CollectionState>(
    savedStateHandle,
    CollectionState()
) {

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
        allRewardedAds.collectionCardPairAd.load()
    }

    private fun onAdDismiss(wasRewardGranted: Boolean) {
        if (wasRewardGranted) {
            rewardCardForAd()
        }
        loadAd(wasRewardGranted)
    }

    private fun rewardCardForAd() {
        unlockRandomCardUseCase().onEach { result ->
            result.onSuccess {
                loadData()
            }
        }.launchIn(viewModelScope)
    }

    private fun loadAd(wasRewardGranted: Boolean = false) {
        if (allRewardedAds.endGameCoinsAd.isLoaded && !wasRewardGranted) {
            loadCardPairs(isAdAvailable = true)
        } else {
            loadCardPairs(isAdAvailable = false)
            allRewardedAds.endGameCoinsAd.load(
                onLoaded = {
                    if (!wasRewardGranted) {
                        loadCardPairs(isAdAvailable = true)
                    }
                },
                onFailed = {
                    loadCardPairs(isAdAvailable = false)
                }
            )
        }
    }

    private fun loadData(animateCoins: Boolean = false) {
        loadAd()
        loadCoins(animateCoins)
        loadCardPairs(isAdAvailable = allRewardedAds.endGameCoinsAd.isLoaded)
    }

    private fun loadCoins(animateCoins: Boolean) {
        getCoinsUseCase().onEach {
            it.onSuccess { coins ->
                sendState { copy(coins = coins, animateCoins = animateCoins) }
            }
        }.launchIn(viewModelScope)
    }

    private fun loadCardPairs(isAdAvailable: Boolean) {
        combine(
            getUnlockedCardPairs()
                .map { it.getOrNull() }
                .filterNotNull(),
            getAllCardPairsCountUseCase()
                .map { it.getOrNull() }
                .filterNotNull(),
            calculateNextCardPairCostUseCase()
                .map { it.getOrNull() }
                .filterNotNull(),
            getUnlockedCardPairsFromAdsCountUseCase()
                .map { it.getOrNull()?.toInt() }
                .filterNotNull()
        ) { unlockedCardPairs, allCardPairsCount, cardPairCost, unlockedCardPairsFromAdsCount ->
            val lockedCardPairsCount = allCardPairsCount - unlockedCardPairs.size
            getCollectionCardPairs(
                unlockedCardPairs,
                lockedCardPairsCount,
                cardPairCost,
                unlockedCardPairsFromAdsCount,
                isAdAvailable
            )
        }
            .distinctUntilChanged()
            .onEach { collectionCardPairs ->
                sendState {
                    copy(
                        collectionCardPairs = collectionCardPairs,
                    )
                }
            }
            .launchIn(viewModelScope)
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
        sendEffect(CollectionEffect.OpenShopScreen)
    }

    private fun onCardPairClick(intent: CollectionIntent.OnCardPairClick) {
        hapticFeedback.vibrateLow()
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
                delay(COINS_SOUND_DELAY)
                coinsPlayer.play()
                loadData(animateCoins = true)
            }
        }.launchIn(viewModelScope)
    }

    private fun onUnlockWithAdClick() {
        hapticFeedback.vibrateLow()
        sendEffect(CollectionEffect.ShowAd(allRewardedAds.endGameCoinsAd))
    }

    companion object {
        const val NO_LOCKED_CARD_PAIRS = 0
        const val LAST_LOCKED_CARD_PAIR = 1
        const val UNLOCK_WITH_ADS_COUNT = 1
        const val UNLOCK_WITH_COINS_COUNT = 1
        const val MAX_UNLOCKED_CARD_PAIRS_WITH_ADS = 3

        const val NUMBER_OF_LOCKED_CARDS_POSSIBLE_TO_UNLOCK =
            UNLOCK_WITH_COINS_COUNT + UNLOCK_WITH_ADS_COUNT

        private const val COINS_SOUND_DELAY = 300L
    }
}
