package com.wojdor.memolki.ui.feature.cardpairdetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.wojdor.memolki.data.mapper.toModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.test.fake.FakeAllCardPairsDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class CardPairDetailsViewModelTest : AppTest() {

    @Inject
    lateinit var savedStateHandle: SavedStateHandle

    private lateinit var viewModel: CardPairDetailsViewModel

    @Before
    override fun setup() {
        super.setup()
        viewModel = CardPairDetailsViewModel(savedStateHandle)
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `When OnCardPairDetailsShow intent is sent then should load card pair`() = runTest {
        viewModel.uiState.test {
            // given
            val cardPair = FakeAllCardPairsDataSource().getAllCardPairs().first()
            val cardPairModel = cardPair.toModel()
            skipItems(1)

            // when
            viewModel.sendIntent(CardPairDetailsIntent.OnCardPairDetailsShow(cardPairModel))

            // then
            assertEquals(cardPairModel, awaitItem().cardPairModel)
        }
    }
}
