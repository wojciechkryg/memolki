package com.wojdor.memolki.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.img_test_half
import com.wojdor.memolki.shared.resources.img_test_whole
import org.jetbrains.compose.resources.painterResource

// TODO(kmp-ios): wire to per-card iOS images when the Resources/Bundle layer ships.
@Composable
actual fun cardImagePainter(imageRes: Int): Painter = when (imageRes) {
    PREVIEW_IMAGE_HALF -> painterResource(Res.drawable.img_test_half)
    else -> painterResource(Res.drawable.img_test_whole)
}
