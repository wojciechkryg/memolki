package com.wojdor.memolki.data.mapper

import com.wojdor.memolki.data.entity.CardPairEntity
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.util.extension.toLinkedSet

fun LinkedHashSet<CardPairEntity>.toModel() = map { it.toModel() }.toLinkedSet()

fun CardPairEntity.toModel() = CardPairModel(
    pair = pair.toModel(id)
)
