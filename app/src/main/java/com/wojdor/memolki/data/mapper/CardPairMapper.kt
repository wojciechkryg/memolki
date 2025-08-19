package com.wojdor.memolki.data.mapper

import com.wojdor.memolki.data.entity.CardPairEntity
import com.wojdor.memolki.domain.model.CardPairModel

fun Set<CardPairEntity>.toModel() = map { it.toModel() }.toSet()

fun CardPairEntity.toModel() = CardPairModel(
    pair = pair.toModel(id)
)
