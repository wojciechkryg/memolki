package com.wojdor.memolki.ui.theme

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

val spacingXS: Dp
    @Composable get() = when {
        isLargeScreen -> 8.dp
        isSmallScreen -> 2.dp
        else -> 4.dp
    }

val spacingS: Dp
    @Composable get() = when {
        isLargeScreen -> 16.dp
        isSmallScreen -> 4.dp
        else -> 8.dp
    }

val spacingM: Dp
    @Composable get() = when {
        isLargeScreen -> 24.dp
        isSmallScreen -> 6.dp
        else -> 12.dp
    }

val spacingL: Dp
    @Composable get() = when {
        isLargeScreen -> 32.dp
        isSmallScreen -> 8.dp
        else -> 16.dp
    }

val spacingXL: Dp
    @Composable get() = when {
        isLargeScreen -> 48.dp
        isSmallScreen -> 12.dp
        else -> 24.dp
    }

val isLargeScreen: Boolean
    @Composable get() = LocalWindowSize.current.widthSizeClass >= WindowWidthSizeClass.Medium

val isSmallScreen: Boolean
    @Composable get() = LocalScreenHeight.current < SMALL_SCREEN_HEIGHT_THRESHOLD

private val SMALL_SCREEN_HEIGHT_THRESHOLD = 750.dp

val LocalScreenHeight = compositionLocalOf { SMALL_SCREEN_HEIGHT_THRESHOLD }

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
val LocalWindowSize = compositionLocalOf {
    WindowSizeClass.calculateFromSize(DpSize(0.dp, 0.dp))
}
