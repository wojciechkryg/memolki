package com.wojdor.memolki.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

fun TextStyle.withColor(color: Color): TextStyle {
    return this.copy(color = color)
}
