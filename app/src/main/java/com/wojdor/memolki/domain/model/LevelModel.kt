package com.wojdor.memolki.domain.model

import android.os.Parcelable
import androidx.annotation.StringRes
import com.wojdor.memolki.R
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class LevelModel(
    val id: String,
    @field:StringRes val textId: Int,
    val columns: Int,
    val rows: Int,
    var isUnlocked: Boolean = false
) : Parcelable {
    data object Empty : LevelModel("", R.string.empty, 0, 0)
    data object Grid2x3 : LevelModel("2x3", R.string.level2x3, 2, 3)
    data object Grid3x4 : LevelModel("3x4", R.string.level3x4, 3, 4)
    data object Grid4x4 : LevelModel("4x4", R.string.level4x4, 4, 4)
    data object Grid4x5 : LevelModel("4x5", R.string.level4x5, 4, 5)
    data object Grid5x6 : LevelModel("5x6", R.string.level5x6, 5, 6)

    fun unlock() {
        isUnlocked = true
    }
}
