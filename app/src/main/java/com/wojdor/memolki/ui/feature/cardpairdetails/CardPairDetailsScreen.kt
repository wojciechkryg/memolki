package com.wojdor.memolki.ui.feature.cardpairdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.ui.feature.cardpairdetails.component.CardPairDetailsContent
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun CardPairDetailsScreen(viewModel: CardPairDetailsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    HandleState(state)
}

@Composable
private fun HandleState(state: CardPairDetailsState) {
    CardPairDetailsScreen(state)
}

@Composable
private fun CardPairDetailsScreen(state: CardPairDetailsState) {
    CardPairDetailsContent(state.cardPairModel)
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
