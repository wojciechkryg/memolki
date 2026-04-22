@file:UseSerializers(StringResourceSerializer::class)

package com.wojdor.memolki.domain.model

import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.board2x3
import com.wojdor.memolki.shared.resources.board3x4
import com.wojdor.memolki.shared.resources.board4x4
import com.wojdor.memolki.shared.resources.board4x5
import com.wojdor.memolki.shared.resources.board4x6
import com.wojdor.memolki.shared.resources.board5x6
import com.wojdor.memolki.shared.resources.empty
import com.wojdor.memolki.util.serializer.StringResourceSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.jetbrains.compose.resources.StringResource

@Serializable
sealed class BoardModel(
    val id: String,
    val textId: StringResource,
    val columns: Int,
    val rows: Int,
) {

    abstract val isUnlocked: Boolean

    @Serializable
    data object Empty : BoardModel("", Res.string.empty, 0, 0) {
        override val isUnlocked: Boolean = false
    }

    @Serializable
    data class Grid2x3(
        override val isUnlocked: Boolean = false
    ) : BoardModel("2x3", Res.string.board2x3, 2, 3)

    @Serializable
    data class Grid3x4(
        override val isUnlocked: Boolean = false
    ) : BoardModel("3x4", Res.string.board3x4, 3, 4)

    @Serializable
    data class Grid4x4(
        override val isUnlocked: Boolean = false
    ) : BoardModel("4x4", Res.string.board4x4, 4, 4)

    @Serializable
    data class Grid4x5(
        override val isUnlocked: Boolean = false
    ) : BoardModel("4x5", Res.string.board4x5, 4, 5)

    @Serializable
    data class Grid4x6(
        override val isUnlocked: Boolean = false
    ) : BoardModel("4x6", Res.string.board4x6, 4, 6)

    @Serializable
    data class Grid5x6(
        override val isUnlocked: Boolean = false
    ) : BoardModel("5x6", Res.string.board5x6, 5, 6)

    companion object {
        const val DEFAULT_LEVEL = 1L
        val DAILY_CHALLENGE = Grid5x6(isUnlocked = true)
    }
}
