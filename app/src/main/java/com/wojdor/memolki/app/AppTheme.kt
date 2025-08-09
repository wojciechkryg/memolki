package com.wojdor.memolki.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme
    ) {
        content()
    }
}

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF263238),
    secondary = Color(0xFF2E7D32),
    tertiary = Color(0xFFCCCCCC),
)
