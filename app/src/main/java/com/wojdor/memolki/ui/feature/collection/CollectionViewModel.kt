package com.wojdor.memolki.ui.feature.collection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.domain.model.CollectionCardPairModel
import com.wojdor.memolki.domain.usecase.GetAllCardPairsCountUseCase
import com.wojdor.memolki.domain.usecase.GetCoinsUseCase
import com.wojdor.memolki.domain.usecase.GetUnlockedCardPairsUseCase
import com.wojdor.memolki.ui.base.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
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
            is CollectionIntent.OnShopClick -> sendEffect(
                CollectionEffect.OpenShopScreen
            )
        }
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
        loadUnlockedCards { unlockedCardPairs ->
            loadLockedCards { allCardParisCount ->
                val lockedCardPairsCount = allCardParisCount - unlockedCardPairs.size
                sendState {
                    copy(
                        collectionCardPairs = getCollectionCardPairs(
                            unlockedCardPairs,
                            lockedCardPairsCount
                        ),
                        allCardPairsCount = allCardParisCount,
                        unlockedCardPairsCount = unlockedCardPairs.size
                    )
                }
            }
        }
    }

    private fun getCollectionCardPairs(
        unlockedCardPairs: List<CardPairModel>,
        lockedCardPairsCount: Int
    ): List<CollectionCardPairModel> =
        (unlockedCardPairs.map { CollectionCardPairModel.Unlocked(it) }
                + CollectionCardPairModel.LockedToUnlockWithAd
                + CollectionCardPairModel.LockedToUnlockWithCoins(CARD_PAIR_COST)
                + List(lockedCardPairsCount - NUMBER_OF_LOCKED_CARDS_POSSIBLE_TO_UNLOCK)
        { CollectionCardPairModel.Locked })

    private fun loadUnlockedCards(onSuccess: (List<CardPairModel>) -> Unit) {
        getUnlockedCardPairs().onEach {
            it.onSuccess { unlockedCardPairs ->
                onSuccess(unlockedCardPairs)
            }
        }.launchIn(viewModelScope)
    }

    private fun loadLockedCards(onSuccess: (Int) -> Unit) {
        getAllCardPairsCountUseCase().onEach {
            it.onSuccess { allCardParisCount ->
                onSuccess(allCardParisCount)
            }
        }.launchIn(viewModelScope)
    }

    companion object {
        const val UNLOCK_WITH_COINS_COUNT = 1
        const val UNLOCK_WITH_ADS_COUNT = 1
        const val CARD_PAIR_COST = 10
        const val NUMBER_OF_LOCKED_CARDS_POSSIBLE_TO_UNLOCK =
            UNLOCK_WITH_COINS_COUNT + UNLOCK_WITH_ADS_COUNT
    }
}
