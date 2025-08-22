package com.wojdor.memolki.ui.components

import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun XmlDrawable(
    modifier: Modifier,
    @DrawableRes drawableRes: Int
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

@Preview
@Composable
private fun XmlDrawablePreview() {
    AppTheme {
        XmlDrawable(
            modifier = Modifier.size(128.dp),
            drawableRes = R.drawable.bg_card_back
        )
    }
}
