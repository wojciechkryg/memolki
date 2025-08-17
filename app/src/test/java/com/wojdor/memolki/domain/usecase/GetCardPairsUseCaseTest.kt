package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.AppTest
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.mockCardPairsLocalDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetCardPairsUseCaseTest : AppTest() {

    private var cardRepository: CardRepository = CardRepository(mockCardPairsLocalDataSource)

    private lateinit var sut: GetCardPairsUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetCardPairsUseCase(testDispatcher, cardRepository)
    }

    @Test
    fun `when repository returns success then use case returns success with the same data`() =
        runTest {
            // when
            sut().test {
                // then
                val expected = Result.success(
                    listOf(
                        CardPairModel(
                            CardModel.Image("banana", 123, 321) to
                                    CardModel.Image("banana", 123, 321)
                        ),
                        CardPairModel(
                            CardModel.Image("apple", 456, 654) to
                                    CardModel.Text("apple", 456)
                        ),
                        CardPairModel(
                            CardModel.Text("orange", 789) to
                                    CardModel.Text("orange", 789)
                        )
                    )
                )
                assertEquals(expected, awaitItem())
                awaitComplete()
            }
        }
}
