package com.wojdor.memolki.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardPairModel(
    val first: CardModel,
    val second: CardModel,
    val addedEpochDay: Long = 0L
) : Parcelable
