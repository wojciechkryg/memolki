package com.wojdor.memolki.ui.theme

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

val spacingXS: Dp
    @Composable @ReadOnlyComposable get() = if (isTablet) 8.dp else 4.dp

val spacingS: Dp
    @Composable @ReadOnlyComposable get() = if (isTablet) 16.dp else 8.dp

val spacingM: Dp
    @Composable @ReadOnlyComposable get() = if (isTablet) 24.dp else 12.dp

val spacingL: Dp
    @Composable @ReadOnlyComposable get() = if (isTablet) 32.dp else 16.dp

val spacingXL: Dp
    @Composable @ReadOnlyComposable get() = if (isTablet) 48.dp else 24.dp

val isTablet: Boolean
    @Composable @ReadOnlyComposable get() = LocalWindowSize.current.widthSizeClass >= WindowWidthSizeClass.Medium

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
val LocalWindowSize = compositionLocalOf {
    WindowSizeClass.calculateFromSize(DpSize(0.dp, 0.dp))
}
