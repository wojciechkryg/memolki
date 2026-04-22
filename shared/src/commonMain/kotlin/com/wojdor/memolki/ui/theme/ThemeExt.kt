package com.wojdor.memolki.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextMotion

fun TextStyle.withColor(color: Color): TextStyle {
    return this.copy(color = color)
}

fun TextStyle.animated(): TextStyle {
    return this.copy(textMotion = TextMotion.Animated)
}
