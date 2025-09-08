package com.wojdor.memolki.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class CollectionCardPairModel : Parcelable {
    data class Unlocked(val cardPair: CardPairModel) : CollectionCardPairModel()
    object LockedToUnlockWithAd : CollectionCardPairModel()
    data class LockedToUnlockWithCoins(val neededCoins: Int) : CollectionCardPairModel()
    object Locked : CollectionCardPairModel()
}
