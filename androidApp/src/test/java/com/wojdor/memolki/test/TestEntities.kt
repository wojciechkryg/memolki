package com.wojdor.memolki.test

import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeEntity

fun dailyChallengeEntity(
    epochDay: Long = 20000L,
    mistakeCount: Int = 0,
    starCount: Int = 3,
    timeMillis: Long = 0L,
    cardFlipCounts: String = ""
) = DailyChallengeEntity(
    epochDay = epochDay,
    mistakeCount = mistakeCount,
    starCount = starCount,
    timeMillis = timeMillis,
    cardFlipCounts = cardFlipCounts
)
