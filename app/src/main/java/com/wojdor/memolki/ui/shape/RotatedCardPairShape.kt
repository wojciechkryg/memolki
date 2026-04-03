package com.wojdor.memolki.ui.shape

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

class RotatedCardPairShape(
    private val cardShape: Shape,
    private val cardWidthFraction: Float,
    private val rotation: Float,
    private val xOffset: Dp,
    private val yOffset: Dp,
    private val aspectRatio: Float
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val cardWidth = size.width * cardWidthFraction
        val cardHeight = cardWidth / aspectRatio
        val cardSize = Size(cardWidth, cardHeight)
        val cardRect = Rect(
            Offset((size.width - cardSize.width) / 2, (size.height - cardSize.height) / 2),
            cardSize
        )
        val xOffsetPx = with(density) { xOffset.toPx() }
        val yOffsetPx = with(density) { yOffset.toPx() }
        val cardOutline = cardShape.createOutline(cardSize, layoutDirection, density)

        val path1 = createTransformedPath(
            cardOutline,
            cardRect,
            rotation,
            xOffset = xOffsetPx,
            yOffset = yOffsetPx
        )
        val path2 = createTransformedPath(
            cardOutline,
            cardRect,
            -rotation,
            xOffset = -xOffsetPx,
            yOffset = yOffsetPx
        )

        val finalPath = Path().apply {
            op(path1, path2, PathOperation.Union)
        }
        return Outline.Generic(finalPath)
    }

    private fun createTransformedPath(
        outline: Outline,
        cardRect: Rect,
        rotation: Float,
        xOffset: Float,
        yOffset: Float,
    ): Path {
        val path = Path().apply { addOutline(outline) }
        val matrix = Matrix()

        // The graphicsLayer modifier applies translation before rotation. Since matrix
        // operations are pre-multiplied, we apply them in reverse order here.
        // The full transformation for a point p is:
        // p' = Rotate(about card center) * Translate(offset) * PlaceInCenter * p
        matrix.translate(cardRect.center.x, cardRect.center.y)
        matrix.rotateZ(rotation)
        matrix.translate(-cardRect.center.x, -cardRect.center.y)

        matrix.translate(xOffset, yOffset)

        matrix.translate(cardRect.topLeft.x, cardRect.topLeft.y)

        path.transform(matrix)
        return path
    }

    private fun Path.addOutline(outline: Outline) {
        when (outline) {
            is Outline.Generic -> addPath(outline.path)
            is Outline.Rectangle -> addRect(outline.rect)
            is Outline.Rounded -> addRoundRect(outline.roundRect)
        }
    }
}
