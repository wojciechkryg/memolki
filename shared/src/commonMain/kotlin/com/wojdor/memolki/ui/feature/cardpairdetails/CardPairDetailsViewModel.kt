package com.wojdor.memolki.ui.feature.cardpairdetails

import androidx.lifecycle.SavedStateHandle
import com.wojdor.memolki.ui.base.MviViewModel

class CardPairDetailsViewModel(
    savedStateHandle: SavedStateHandle
) : MviViewModel<CardPairDetailsIntent, CardPairDetailsState>(
    savedStateHandle,
    CardPairDetailsState.serializer(),
    CardPairDetailsState()
) {

    override fun onIntent(intent: CardPairDetailsIntent) {
        when (intent) {
            is CardPairDetailsIntent.OnCardPairDetailsShow -> sendState {
                copy(
                    cardPairModels = intent.cardPairModels,
                    initialPage = intent.initialPage
                )
            }
        }
    }
}
