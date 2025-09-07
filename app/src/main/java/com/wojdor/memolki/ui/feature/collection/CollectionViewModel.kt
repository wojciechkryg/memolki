package com.wojdor.memolki.ui.feature.collection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.model.CardPairModel
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
                        unlockedCardPairs = unlockedCardPairs,
                        lockedCardPairsCount = lockedCardPairsCount
                    )
                }
            }
        }
    }

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
}
