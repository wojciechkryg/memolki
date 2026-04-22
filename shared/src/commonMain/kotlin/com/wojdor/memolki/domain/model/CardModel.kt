@file:UseSerializers(StringResourceSerializer::class)

package com.wojdor.memolki.domain.model

import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.empty
import com.wojdor.memolki.util.serializer.StringResourceSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.jetbrains.compose.resources.StringResource

@Serializable
sealed class CardModel {

    abstract val id: String
    abstract val pairId: String
    abstract val textRes: StringResource
    abstract val isFlippedFront: Boolean
    abstract val isPairMatched: Boolean
    abstract val isMatchAnimating: Boolean
    abstract val isMistakeShaking: Boolean

    fun copyState(
        isFlippedFront: Boolean = this.isFlippedFront,
        isPairMatched: Boolean = this.isPairMatched,
        isMatchAnimating: Boolean = this.isMatchAnimating,
        isMistakeShaking: Boolean = this.isMistakeShaking
    ): CardModel = when (this) {
        is Text -> copy(
            isFlippedFront = isFlippedFront,
            isPairMatched = isPairMatched,
            isMatchAnimating = isMatchAnimating,
            isMistakeShaking = isMistakeShaking
        )

        is Image -> copy(
            isFlippedFront = isFlippedFront,
            isPairMatched = isPairMatched,
            isMatchAnimating = isMatchAnimating,
            isMistakeShaking = isMistakeShaking
        )

        Empty -> this
    }

    @Serializable
    object Empty : CardModel() {
        override val id: String = ""
        override val pairId: String = ""
        override val textRes: StringResource = Res.string.empty
        override val isFlippedFront: Boolean = false
        override val isPairMatched: Boolean = false
        override val isMatchAnimating: Boolean = false
        override val isMistakeShaking: Boolean = false
    }

    @Serializable
    data class Text(
        override val id: String,
        override val pairId: String,
        override val textRes: StringResource,
        override val isFlippedFront: Boolean = false,
        override val isPairMatched: Boolean = false,
        override val isMatchAnimating: Boolean = false,
        override val isMistakeShaking: Boolean = false
    ) : CardModel()

    @Serializable
    data class Image(
        override val id: String,
        override val pairId: String,
        override val textRes: StringResource,
        val imageRes: Int,
        override val isFlippedFront: Boolean = false,
        override val isPairMatched: Boolean = false,
        override val isMatchAnimating: Boolean = false,
        override val isMistakeShaking: Boolean = false
    ) : CardModel()
}
