package com.wojdor.memolki.data.mapper

import com.wojdor.memolki.data.entity.CardEntity
import com.wojdor.memolki.domain.model.CardModel

fun CardEntity.toModel(pairId: String) = when (this) {
    is CardEntity.Text -> CardModel.Text(
        id = id,
        pairId = pairId,
        textRes = textRes
    )

    is CardEntity.Image -> CardModel.Image(
        id = id,
        pairId = pairId,
        textRes = textRes,
        imageRes = imageRes
    )
}
