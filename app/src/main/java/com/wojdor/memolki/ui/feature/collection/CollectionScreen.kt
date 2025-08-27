package com.wojdor.memolki.ui.feature.collection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.feature.collection.component.CoinsAmount
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun CollectionScreen(
    viewModel: CollectionViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()
    HandleEffect(viewModel, navController)
    HandleState(viewModel, state)
}

@Composable
private fun HandleEffect(
    viewModel: CollectionViewModel,
    navController: NavController
) {
    CollectUiEffects(viewModel) {
        // TODO: Handle game effects here if needed
    }
}

@Composable
private fun HandleState(
    viewModel: CollectionViewModel,
    state: CollectionState
) {
    val callbacks = CollectionCallbacks()
    CollectionScreen(state, callbacks)
}

@Composable
fun CollectionScreen(
    state: CollectionState,
    callbacks: CollectionCallbacks = CollectionCallbacks()
) {
    Box(modifier = Modifier.fillMaxSize()) {
        CoinsAmount(
            modifier = Modifier.align(Alignment.TopStart),
            state = state
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun EndGamePreview() {
    AppTheme {
        CollectionScreen(
            state = CollectionState(coins = 1234)
        )
    }
}
