package com.wojdor.memolki.domain.model

import androidx.annotation.StringRes
import com.wojdor.memolki.R
import kotlinx.serialization.Serializable

// TODO(compose-resources): move to commonMain once Phase 13 lets us replace R.string.* in subclass super-constructors with Res.*
@Serializable
sealed class BoardModel(
    val id: String,
    @field:StringRes val textId: Int,
    val columns: Int,
    val rows: Int,
) {

    abstract val isUnlocked: Boolean

    @Serializable
    data object Empty : BoardModel("", R.string.empty, 0, 0) {
        override val isUnlocked: Boolean = false
    }

    @Serializable
    data class Grid2x3(
        override val isUnlocked: Boolean = false
    ) : BoardModel("2x3", R.string.board2x3, 2, 3)

    @Serializable
    data class Grid3x4(
        override val isUnlocked: Boolean = false
    ) : BoardModel("3x4", R.string.board3x4, 3, 4)

    @Serializable
    data class Grid4x4(
        override val isUnlocked: Boolean = false
    ) : BoardModel("4x4", R.string.board4x4, 4, 4)

    @Serializable
    data class Grid4x5(
        override val isUnlocked: Boolean = false
    ) : BoardModel("4x5", R.string.board4x5, 4, 5)

    @Serializable
    data class Grid4x6(
        override val isUnlocked: Boolean = false
    ) : BoardModel("4x6", R.string.board4x6, 4, 6)

    @Serializable
    data class Grid5x6(
        override val isUnlocked: Boolean = false
    ) : BoardModel("5x6", R.string.board5x6, 5, 6)

    companion object {
        const val DEFAULT_LEVEL = 1L
        val DAILY_CHALLENGE = Grid5x6(isUnlocked = true)
    }
}
