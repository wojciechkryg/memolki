package com.wojdor.memolki.data.mapper

import com.wojdor.memolki.data.entity.CardPairEntity
import com.wojdor.memolki.domain.model.CardPairModel

fun List<CardPairEntity>.toModel() = map { it.toModel() }

fun CardPairEntity.toModel() = CardPairModel(
    pair = pair.toModel(id)
)
