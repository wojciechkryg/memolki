package com.wojdor.memolki.domain.model

import android.os.Parcelable
import androidx.annotation.StringRes
import com.wojdor.memolki.R
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class BoardModel(
    val id: String,
    @field:StringRes val textId: Int,
    val columns: Int,
    val rows: Int,
) : Parcelable {

    abstract val isUnlocked: Boolean

    data object Empty : BoardModel("", R.string.empty, 0, 0) {
        @IgnoredOnParcel
        override val isUnlocked: Boolean = false
    }

    data class Grid2x3(
        override val isUnlocked: Boolean = false
    ) : BoardModel("2x3", R.string.board2x3, 2, 3)

    data class Grid3x4(
        override val isUnlocked: Boolean = false
    ) : BoardModel("3x4", R.string.board3x4, 3, 4)

    data class Grid4x4(
        override val isUnlocked: Boolean = false
    ) : BoardModel("4x4", R.string.board4x4, 4, 4)

    data class Grid4x5(
        override val isUnlocked: Boolean = false
    ) : BoardModel("4x5", R.string.board4x5, 4, 5)

    data class Grid4x6(
        override val isUnlocked: Boolean = false
    ) : BoardModel("4x6", R.string.board4x6, 4, 6)

    data class Grid5x6(
        override val isUnlocked: Boolean = false
    ) : BoardModel("5x6", R.string.board5x6, 5, 6)

    companion object {
        const val DEFAULT_LEVEL = 1L
        val DAILY_CHALLENGE = Grid5x6(isUnlocked = true)
    }
}
