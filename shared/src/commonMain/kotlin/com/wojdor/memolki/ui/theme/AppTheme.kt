package com.wojdor.memolki.ui.theme

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val fontColor = AppColors.Font
    MaterialTheme(
        colorScheme = lightColorScheme(
            onPrimary = fontColor,
            onPrimaryContainer = fontColor,
            onSecondary = fontColor,
            onSecondaryContainer = fontColor,
            onTertiary = fontColor,
            onTertiaryContainer = fontColor,
            onBackground = fontColor,
            onSurface = fontColor,
            onSurfaceVariant = fontColor
        ),
        typography = appTypography()
    ) {
        CompositionLocalProvider(LocalContentColor provides fontColor) {
            content()
        }
    }
}
