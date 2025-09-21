package com.wojdor.memolki.ui.feature.collection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.domain.model.CollectionCardPairModel
import com.wojdor.memolki.domain.usecase.GetAllCardPairsCountUseCase
import com.wojdor.memolki.domain.usecase.GetCoinsUseCase
import com.wojdor.memolki.domain.usecase.GetUnlockedCardPairsUseCase
import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.util.media.HapticFeedback
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val hapticFeedback: HapticFeedback,
    private val getCoinsUseCase: GetCoinsUseCase,
    private val getUnlockedCardPairs: GetUnlockedCardPairsUseCase,
    private val getAllCardPairsCountUseCase: GetAllCardPairsCountUseCase
) : MviViewModel<CollectionIntent, CollectionState>(
    savedStateHandle,
    CollectionState()
) {

    init {
        loadData()
    }

    override fun onIntent(intent: CollectionIntent) {
        when (intent) {
            is CollectionIntent.OnShopClick -> onShopClick()
            is CollectionIntent.OnCardPairClick -> onCardPairClick(intent)
        }
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

    private fun loadData() {
        loadCoins()
        loadCardPairs()
    }

    private fun loadCoins() {
        getCoinsUseCase().onEach {
            it.onSuccess { coins ->
                sendState { copy(coins = coins) }
            }
        }.launchIn(viewModelScope)
    }

    private fun loadCardPairs() {
        combine(
            getUnlockedCardPairs()
                .map { it.getOrNull() }
                .filterNotNull(),
            getAllCardPairsCountUseCase()
                .map { it.getOrNull() }
                .filterNotNull()
        ) { unlockedCardPairs, allCardPairsCount ->
            val lockedCardPairsCount = allCardPairsCount - unlockedCardPairs.size
            unlockedCardPairs to lockedCardPairsCount
        }
            .distinctUntilChanged()
            .onEach { (unlockedCardPairs, lockedCardPairsCount) ->
                sendState {
                    copy(
                        collectionCardPairs = getCollectionCardPairs(
                            unlockedCardPairs,
                            lockedCardPairsCount
                        ),
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getCollectionCardPairs(
        unlockedCardPairs: List<CardPairModel>,
        lockedCardPairsCount: Int
    ): List<CollectionCardPairModel> {
        val unlocked = unlockedCardPairs.map { CollectionCardPairModel.Unlocked(it) }
        val lockedToUnlockWithAd = getLockedToUnlockWithAd(lockedCardPairsCount)
        val lockedToUnlockWithCoins = getLockedToUnlockWithCoins(lockedCardPairsCount)
        val lockedCardPairsNotPossibleToUnlockYet =
            getLockedNotPossibleToUnlockYet(lockedCardPairsCount)
        return unlocked +
                lockedToUnlockWithAd +
                lockedToUnlockWithCoins +
                lockedCardPairsNotPossibleToUnlockYet
    }

    private fun getLockedToUnlockWithAd(lockedCardPairsCount: Int)
            : List<CollectionCardPairModel.LockedToUnlockWithAd> =
        if (lockedCardPairsCount > 0) {
            List(UNLOCK_WITH_ADS_COUNT) { CollectionCardPairModel.LockedToUnlockWithAd }
        } else {
            emptyList()
        }

    private fun getLockedToUnlockWithCoins(lockedCardPairsCount: Int)
            : List<CollectionCardPairModel.LockedToUnlockWithCoins> =
        if (lockedCardPairsCount > 1) {
            List(UNLOCK_WITH_COINS_COUNT) {
                CollectionCardPairModel.LockedToUnlockWithCoins(
                    CARD_PAIR_COST
                )
            }
        } else {
            emptyList()
        }

    private fun getLockedNotPossibleToUnlockYet(lockedCardPairsCount: Int)
            : List<CollectionCardPairModel.Locked> {
        val lockedCardPairsNotPossibleToUnlockYet =
            (lockedCardPairsCount - NUMBER_OF_LOCKED_CARDS_POSSIBLE_TO_UNLOCK).coerceAtLeast(0)
        return List(lockedCardPairsNotPossibleToUnlockYet) { CollectionCardPairModel.Locked }
    }

    companion object {
        const val UNLOCK_WITH_ADS_COUNT = 1
        const val UNLOCK_WITH_COINS_COUNT = 1

        // TODO: Make cost dynamic
        const val CARD_PAIR_COST = 10
        const val NUMBER_OF_LOCKED_CARDS_POSSIBLE_TO_UNLOCK =
            UNLOCK_WITH_COINS_COUNT + UNLOCK_WITH_ADS_COUNT
    }
}
