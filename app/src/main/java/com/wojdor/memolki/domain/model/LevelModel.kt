package com.wojdor.memolki.domain.model

import androidx.annotation.StringRes
import com.wojdor.memolki.R

sealed class LevelModel(
    val id: String,
    @StringRes val textId: Int,
    val columns: Int,
    val rows: Int,
    val isUnlocked: Boolean,
) {
    object Empty : LevelModel("", R.string.empty, 0, 0, isUnlocked = false)
    object Grid2x3 : LevelModel("2x3", R.string.level2x3, 2, 3, isUnlocked = true)
    object Grid3x4 : LevelModel("3x4", R.string.level3x4, 3, 4, isUnlocked = false)
    object Grid4x4 : LevelModel("4x4", R.string.level4x4, 4, 4, isUnlocked = false)
    object Grid4x5 : LevelModel("4x5", R.string.level4x5, 4, 5, isUnlocked = false)
    object Grid5x6 : LevelModel("5x5", R.string.level5x6, 5, 6, isUnlocked = false)
}
