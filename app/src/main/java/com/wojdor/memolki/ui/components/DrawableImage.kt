package com.wojdor.memolki.ui.components

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext

@Composable
fun DrawableImage(
    modifier: Modifier,
    drawableRes: Int
) {
    val context = LocalContext.current
    val drawable = remember(drawableRes) {
        AppCompatResources.getDrawable(context, drawableRes)
    }
    Canvas(
        modifier = modifier,
        onDraw = {
            drawable?.let {
                it.setBounds(0, 0, size.width.toInt(), size.height.toInt())
                it.draw(drawContext.canvas.nativeCanvas)
            }
        }
    )
}
