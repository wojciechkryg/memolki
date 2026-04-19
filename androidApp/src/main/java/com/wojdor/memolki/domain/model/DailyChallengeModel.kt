package com.wojdor.memolki.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class DailyChallengeModel(
    val epochDay: Long = 0L,
    val mistakeCount: Int = 0,
    val starCount: Int = 0,
    val timeMillis: Long = 0L,
    val cardFlipCounts: List<List<Int>> = emptyList()
)
