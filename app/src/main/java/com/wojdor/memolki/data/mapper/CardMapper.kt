package com.wojdor.memolki.data.mapper

import com.wojdor.memolki.data.entity.CardEntity
import com.wojdor.memolki.domain.model.CardModel

fun Pair<CardEntity, CardEntity>.toModel(pairId: String) = Pair(
    first = first.toModel(pairId),
    second = second.toModel(pairId)
)

fun CardEntity.toModel(pairId: String) = when (this) {
    is CardEntity.Text -> CardModel.Text(
        pairId = pairId,
        textRes = textRes
    )

    is CardEntity.Image -> CardModel.Image(
        pairId = pairId,
        textRes = textRes,
        imageRes = imageRes
    )
}
