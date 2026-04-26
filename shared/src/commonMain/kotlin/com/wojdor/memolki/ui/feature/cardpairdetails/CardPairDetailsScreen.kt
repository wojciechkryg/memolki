package com.wojdor.memolki.ui.feature.cardpairdetails

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.wojdor.memolki.shared.resources.*
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.ui.component.PREVIEW_IMAGE_HALF
import com.wojdor.memolki.ui.component.PREVIEW_IMAGE_WHOLE
import com.wojdor.memolki.ui.component.PreviewBackground
import com.wojdor.memolki.ui.feature.cardpairdetails.component.CardPairDetailsContent
import com.wojdor.memolki.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

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

@Preview
@Composable
private fun CardPairDetailsScreenPreview() {
    AppTheme {
        PreviewBackground {
            CardPairDetailsScreen(
                state = CardPairDetailsState(
                    listOf(
                        CardPairModel(
                            first = CardModel.Image(
                                "banana_whole",
                                "banana",
                                Res.string.banana,
                                PREVIEW_IMAGE_WHOLE
                            ),
                            second = CardModel.Image(
                                "banana_half",
                                "banana",
                                Res.string.banana,
                                PREVIEW_IMAGE_HALF
                            )
                        )
                    )
                )
            )
        }
    }
}

@Preview
@Composable
private fun CardPairDetailsScreenDifferentTextsPreview() {
    AppTheme {
        PreviewBackground {
            CardPairDetailsScreen(
                state = CardPairDetailsState(
                    listOf(
                        CardPairModel(
                            first = CardModel.Image(
                                "banana_whole",
                                "banana",
                                Res.string.banana,
                                PREVIEW_IMAGE_WHOLE
                            ),
                            second = CardModel.Image(
                                "banana_half",
                                "banana",
                                Res.string.apple,
                                PREVIEW_IMAGE_HALF
                            )
                        )
                    )
                )
            )
        }
    }
}
