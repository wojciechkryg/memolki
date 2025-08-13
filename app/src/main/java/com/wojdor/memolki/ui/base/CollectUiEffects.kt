package com.wojdor.memolki.ui.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun CollectUiEffects(viewModel: MviViewModel<*, *>, onUiEffect: (UiEffect) -> Unit) {
    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { onUiEffect(it) }
    }
}
