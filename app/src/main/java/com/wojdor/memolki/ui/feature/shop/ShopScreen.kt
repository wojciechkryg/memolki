package com.wojdor.memolki.ui.feature.shop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun ShopScreen(
    viewModel: ShopViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()
    HandleEffect(viewModel, navController)
    HandleState(viewModel, state)
}

@Composable
private fun HandleEffect(
    viewModel: ShopViewModel,
    navController: NavController
) {
    CollectUiEffects(viewModel) {
        when (it) {
            else -> TODO()
        }
    }
}

@Composable
private fun HandleState(
    viewModel: ShopViewModel,
    state: ShopState
) {
    val callbacks = ShopCallbacks()
    ShopScreen(state, callbacks)
}

@Composable
private fun ShopScreen(
    state: ShopState,
    callbacks: ShopCallbacks = ShopCallbacks()
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(stringResource(R.string.shop))
    }
}

@Preview(showBackground = true)
@Composable
private fun ShopScreenPreview() {
    AppTheme {
        ShopScreen(
            state = ShopState()
        )
    }
}
