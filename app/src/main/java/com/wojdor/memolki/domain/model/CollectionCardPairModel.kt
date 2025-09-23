package com.wojdor.memolki.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class CollectionCardPairModel : Parcelable {
    data class Unlocked(val cardPair: CardPairModel) : CollectionCardPairModel()
    data class LockedToUnlockWithCoins(val coins: Int) : CollectionCardPairModel()
    object LockedToUnlockWithAd : CollectionCardPairModel()
    object Locked : CollectionCardPairModel()
}
