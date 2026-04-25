package com.wojdor.memolki.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

@Composable
expect fun cardImagePainter(imageRes: Int): Painter
