package com.wojdor.memolki.data.mapper

import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeEntity
import com.wojdor.memolki.domain.model.DailyChallengeModel

fun DailyChallengeEntity.toModel() = DailyChallengeModel(
    epochDay = epochDay,
    mistakeCount = mistakeCount,
    starCount = starCount,
    timeMillis = timeMillis,
    cardFlipCounts = CardFlipCountsMapper.deserialize(cardFlipCounts)
)

fun DailyChallengeModel.toEntity(epochDay: Long) = DailyChallengeEntity(
    epochDay = epochDay,
    mistakeCount = mistakeCount,
    starCount = starCount,
    timeMillis = timeMillis,
    cardFlipCounts = CardFlipCountsMapper.serialize(cardFlipCounts)
)
