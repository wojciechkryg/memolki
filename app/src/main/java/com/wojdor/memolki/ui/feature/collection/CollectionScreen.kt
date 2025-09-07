package com.wojdor.memolki.ui.feature.collection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import com.wojdor.memolki.ui.feature.collection.component.ShopButton
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
    CollectUiEffects(viewModel) {}
}

@Composable
private fun HandleState(
    viewModel: CollectionViewModel,
    state: CollectionState
) {
    val callbacks = CollectionCallbacks(
        onShopButtonClick = {
            // TODO: Navigate to shop screen
        }
    )
    CollectionScreen(state, callbacks)
}

@Composable
fun CollectionScreen(
    state: CollectionState,
    callbacks: CollectionCallbacks = CollectionCallbacks()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CoinsAmount(
                modifier = Modifier.weight(1f),
                state = state
            )
            ShopButton(
                onClick = callbacks.onShopButtonClick
            )
        }
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
