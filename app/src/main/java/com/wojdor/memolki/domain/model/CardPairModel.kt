package com.wojdor.memolki.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardPairModel(
    val pair: Pair<CardModel, CardModel>
) : Parcelable
