package com.wojdor.memolki.domain.model

import kotlinx.serialization.Serializable

@Serializable
sealed class CollectionCardPairModel {
    @Serializable
    data class Unlocked(val cardPair: CardPairModel) : CollectionCardPairModel()

    @Serializable
    data class LockedToUnlockWithCoins(val coins: Int) : CollectionCardPairModel()

    @Serializable
    object LockedToUnlockWithAd : CollectionCardPairModel()

    @Serializable
    object Locked : CollectionCardPairModel()
}
