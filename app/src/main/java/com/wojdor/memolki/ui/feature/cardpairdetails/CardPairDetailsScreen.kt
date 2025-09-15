package com.wojdor.memolki.ui.feature.cardpairdetails

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun CardPairDetailsScreen(
    viewModel: CardPairDetailsViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()
    HandleEffect(viewModel, navController)
    HandleState(viewModel, state)
}

@Composable
private fun HandleEffect(
    viewModel: CardPairDetailsViewModel,
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
    viewModel: CardPairDetailsViewModel,
    state: CardPairDetailsState
) {
    val callbacks = CardPairDetailsCallbacks()
    CardPairDetailsScreen(state, callbacks)
}

@Composable
private fun CardPairDetailsScreen(
    state: CardPairDetailsState,
    callbacks: CardPairDetailsCallbacks = CardPairDetailsCallbacks()
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(stringResource(state.cardPairModel.pair.first.textRes))
    }
}

@Preview(showBackground = true)
@Composable
private fun CardPairDetailsScreenPreview() {
    AppTheme {
        CardPairDetailsScreen(
            state = CardPairDetailsState(
                CardPairModel(
                    CardModel.Image(
                        "banana_whole",
                        "banana",
                        R.string.banana,
                        R.drawable.img_banana_whole
                    ) to
                            CardModel.Image(
                                "banana_half",
                                "banana",
                                R.string.banana,
                                R.drawable.img_banana_half
                            )
                )
            )
        )
    }
}
