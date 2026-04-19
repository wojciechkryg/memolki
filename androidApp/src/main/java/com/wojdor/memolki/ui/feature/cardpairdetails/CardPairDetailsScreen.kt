package com.wojdor.memolki.ui.feature.cardpairdetails

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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
    if (state.cardPairModels.isEmpty()) return
    val pagerState = rememberPagerState(
        initialPage = state.initialPage,
        pageCount = { state.cardPairModels.size }
    )
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        CardPairDetailsContent(state.cardPairModels[page])
    }
}

@Preview(showBackground = true)
@Composable
private fun CardPairDetailsScreenPreview() {
    AppTheme {
        CardPairDetailsScreen(
            state = CardPairDetailsState(
                listOf(
                    CardPairModel(
                        first = CardModel.Image(
                            "banana_whole",
                            "banana",
                            R.string.app_name,
                            R.drawable.img_test_whole
                        ),
                        second = CardModel.Image(
                            "banana_half",
                            "banana",
                            R.string.app_name,
                            R.drawable.img_test_half
                        )
                    )
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CardPairDetailsScreenDifferentTextsPreview() {
    AppTheme {
        CardPairDetailsScreen(
            state = CardPairDetailsState(
                listOf(
                    CardPairModel(
                        first = CardModel.Image(
                            "banana_whole",
                            "banana",
                            R.string.menu,
                            R.drawable.img_test_whole
                        ),
                        second = CardModel.Image(
                            "banana_half",
                            "banana",
                            R.string.new_game,
                            R.drawable.img_test_half
                        )
                    )
                )
            )
        )
    }
}
