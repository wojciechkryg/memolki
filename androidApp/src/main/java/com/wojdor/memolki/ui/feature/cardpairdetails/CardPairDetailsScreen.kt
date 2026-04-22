package com.wojdor.memolki.ui.feature.cardpairdetails

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel
import com.wojdor.memolki.R
import com.wojdor.memolki.shared.resources.*
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.ui.feature.cardpairdetails.component.CardPairDetailsContent
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun CardPairDetailsScreen(viewModel: CardPairDetailsViewModel = koinViewModel()) {
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
                            Res.string.banana,
                            R.drawable.img_test_whole
                        ),
                        second = CardModel.Image(
                            "banana_half",
                            "banana",
                            Res.string.banana,
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
                            Res.string.banana,
                            R.drawable.img_test_whole
                        ),
                        second = CardModel.Image(
                            "banana_half",
                            "banana",
                            Res.string.apple,
                            R.drawable.img_test_half
                        )
                    )
                )
            )
        )
    }
}
