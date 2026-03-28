package com.wojdor.memolki.ui.component

import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun XmlDrawable(
    modifier: Modifier,
    @DrawableRes drawableRes: Int,
    alignment: Alignment = Alignment.Center,
    contentDescription: String? = null
) {
    val context = LocalContext.current
    val drawable = remember(drawableRes) {
        AppCompatResources.getDrawable(context, drawableRes)
    }
    val semanticsModifier = if (contentDescription != null) {
        modifier.semantics { this.contentDescription = contentDescription }
    } else {
        modifier
    }
    Canvas(
        modifier = semanticsModifier,
        onDraw = {
            drawable?.let {
                val canvasWidth = size.width.toInt()
                val canvasHeight = size.height.toInt()
                val drawableWidth = it.intrinsicWidth
                val drawableHeight = it.intrinsicHeight

                if (drawableWidth <= 0 || drawableHeight <= 0) {
                    it.setBounds(0, 0, canvasWidth, canvasHeight)
                } else {
                    val drawableRatio = drawableWidth.toFloat() / drawableHeight
                    val canvasRatio = canvasWidth.toFloat() / canvasHeight

                    val finalWidth: Int
                    val finalHeight: Int
                    if (drawableRatio > canvasRatio) {
                        finalWidth = canvasWidth
                        finalHeight = (canvasWidth / drawableRatio).toInt()
                    } else {
                        finalHeight = canvasHeight
                        finalWidth = (canvasHeight * drawableRatio).toInt()
                    }

                    val drawableSize = IntSize(finalWidth, finalHeight)
                    val canvasSize = IntSize(canvasWidth, canvasHeight)
                    val offset = alignment.align(drawableSize, canvasSize, LayoutDirection.Ltr)
                    it.setBounds(offset.x, offset.y, offset.x + finalWidth, offset.y + finalHeight)
                }
                it.draw(drawContext.canvas.nativeCanvas)
            }
        }
    )
}

@Preview
@Composable
private fun XmlDrawablePreview() {
    AppTheme {
        XmlDrawable(
            modifier = Modifier.size(128.dp),
            drawableRes = R.drawable.bg_card_back,
            alignment = Alignment.BottomCenter
        )
    }
}
