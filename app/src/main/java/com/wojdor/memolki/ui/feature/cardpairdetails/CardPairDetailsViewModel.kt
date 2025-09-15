package com.wojdor.memolki.ui.feature.cardpairdetails

import androidx.lifecycle.SavedStateHandle
import com.wojdor.memolki.ui.base.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CardPairDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : MviViewModel<CardPairDetailsIntent, CardPairDetailsState>(
    savedStateHandle,
    CardPairDetailsState()
) {

    override fun onIntent(intent: CardPairDetailsIntent) {
        when (intent) {
            is CardPairDetailsIntent.OnCardPairDetailsShow -> sendState { copy(intent.cardPairModel) }
        }
    }
}
