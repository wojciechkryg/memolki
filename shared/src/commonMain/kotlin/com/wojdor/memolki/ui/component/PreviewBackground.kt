package com.wojdor.memolki.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun PreviewBackground(content: @Composable () -> Unit) {
    Box(modifier = Modifier.background(Color.White)) {
        content()
    }
}
