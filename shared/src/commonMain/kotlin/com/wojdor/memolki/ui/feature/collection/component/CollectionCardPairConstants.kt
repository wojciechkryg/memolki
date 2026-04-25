package com.wojdor.memolki.ui.feature.collection.component

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.ui.shape.RotatedCardPairShape
import com.wojdor.memolki.ui.theme.CardShape

internal const val CARD_SIZE_FRACTION = 0.65f
internal val CARD_OFFSET_HORIZONTAL = 24.dp
internal val CARD_OFFSET_VERTICAL = 4.dp
internal const val CARD_ROTATION = 10f
internal val CARD_PAIR_VERTICAL_PADDING = CARD_ROTATION.dp

internal val cardLeftModifier = Modifier
    .fillMaxWidth(CARD_SIZE_FRACTION)
    .aspectRatio(1f)
    .rotate(CARD_ROTATION)
    .offset(x = CARD_OFFSET_HORIZONTAL, y = -CARD_OFFSET_VERTICAL)

internal val cardRightModifier = Modifier
    .fillMaxWidth(CARD_SIZE_FRACTION)
    .aspectRatio(1f)
    .rotate(-CARD_ROTATION)
    .offset(x = -CARD_OFFSET_HORIZONTAL, y = -CARD_OFFSET_VERTICAL)

internal val rotatedCardPairShape = RotatedCardPairShape(
    cardShape = CardShape,
    cardWidthFraction = CARD_SIZE_FRACTION,
    rotation = CARD_ROTATION,
    xOffset = CARD_OFFSET_HORIZONTAL,
    yOffset = -CARD_OFFSET_VERTICAL,
    aspectRatio = 1f
)
