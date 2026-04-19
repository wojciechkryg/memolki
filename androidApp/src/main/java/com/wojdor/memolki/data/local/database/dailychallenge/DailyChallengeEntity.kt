package com.wojdor.memolki.data.local.database.dailychallenge

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_challenges")
data class DailyChallengeEntity(
    @PrimaryKey val epochDay: Long,
    val mistakeCount: Int,
    val starCount: Int,
    val timeMillis: Long,
    val cardFlipCounts: String
)
