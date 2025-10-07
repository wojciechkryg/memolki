package com.wojdor.memolki.ui.feature.cardpairdetails

import app.cash.turbine.test
import com.wojdor.memolki.data.mapper.toModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockAllCardPairsDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class CardPairDetailsViewModelTest : AppTest() {

    private lateinit var viewModel: CardPairDetailsViewModel

    override fun setup() {
        super.setup()
        viewModel = CardPairDetailsViewModel(savedStateHandle)
    }

    @Test
    fun `When OnCardPairDetailsShow intent is sent then should load card pair`() = runTest {
        viewModel.uiState.test {
            // given
            val cardPair = MockAllCardPairsDataSource.getAllCardPairs().first()
            val cardPairModel = cardPair.toModel()
            skipItems(1)

            // when
            viewModel.sendIntent(CardPairDetailsIntent.OnCardPairDetailsShow(cardPairModel))

            // then
            assertEquals(cardPairModel, awaitItem().cardPairModel)
        }
    }
}
