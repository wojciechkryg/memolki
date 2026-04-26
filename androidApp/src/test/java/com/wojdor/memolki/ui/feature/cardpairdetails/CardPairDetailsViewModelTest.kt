package com.wojdor.memolki.ui.feature.cardpairdetails

import app.cash.turbine.test
import com.wojdor.memolki.data.mapper.toModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.fake.FakeAllCardPairsDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get

@ExperimentalCoroutinesApi
class CardPairDetailsViewModelTest : AppTest() {

    private lateinit var viewModel: CardPairDetailsViewModel

    @BeforeTest
    override fun setup() {
        super.setup()
        viewModel = get()
    }

    @Test
    fun `when OnCardPairDetailsShow intent is sent then should load card pairs`() = runTest {
        viewModel.uiState.test {
            // given
            val cardPairs = FakeAllCardPairsDataSource().getAllCardPairs()
            val cardPairModels = cardPairs.map { it.toModel() }
            val initialPage = 1
            skipItems(1)

            // when
            viewModel.sendIntent(
                CardPairDetailsIntent.OnCardPairDetailsShow(
                    cardPairModels,
                    initialPage
                )
            )

            // then
            val state = awaitItem()
            assertEquals(cardPairModels, state.cardPairModels)
            assertEquals(initialPage, state.initialPage)
        }
    }
}
