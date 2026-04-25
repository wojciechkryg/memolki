package com.wojdor.memolki.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.img_test_half
import com.wojdor.memolki.shared.resources.img_test_whole

@Composable
actual fun cardImagePainter(imageRes: Int): Painter = when (imageRes) {
    PREVIEW_IMAGE_WHOLE -> org.jetbrains.compose.resources.painterResource(Res.drawable.img_test_whole)
    PREVIEW_IMAGE_HALF -> org.jetbrains.compose.resources.painterResource(Res.drawable.img_test_half)
    else -> androidx.compose.ui.res.painterResource(imageRes)
}
