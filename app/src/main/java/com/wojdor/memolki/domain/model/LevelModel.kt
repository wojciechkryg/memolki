package com.wojdor.memolki.domain.model

import androidx.annotation.StringRes
import com.wojdor.memolki.R

sealed class LevelModel(
    @StringRes val textId: Int,
    val columns: Int,
    val rows: Int,
    val isUnlocked: Boolean,
) {
    object NoLevel : LevelModel(R.string.empty, 0, 0, isUnlocked = false)
    object Level2x3 : LevelModel(R.string.level2x3, 2, 3, isUnlocked = true)
    object Level3x4 : LevelModel(R.string.level3x4, 3, 4, isUnlocked = false)
    object Level4x4 : LevelModel(R.string.level4x4, 4, 4, isUnlocked = false)
    object Level4x5 : LevelModel(R.string.level4x5, 4, 5, isUnlocked = false)
    object Level5x6 : LevelModel(R.string.level5x6, 5, 6, isUnlocked = false)
}
