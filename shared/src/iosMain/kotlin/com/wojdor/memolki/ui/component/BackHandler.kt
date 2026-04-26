package com.wojdor.memolki.ui.component

import androidx.compose.runtime.Composable

// TODO(kmp-ios): wire to UINavigationController back gesture / interactive pop when iOS ships.
@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) = Unit
