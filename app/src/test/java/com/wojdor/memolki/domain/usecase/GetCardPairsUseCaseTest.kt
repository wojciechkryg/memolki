package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.data.local.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockAllCardPairsDataSource
import com.wojdor.memolki.test.mock.MockDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetCardPairsUseCaseTest : AppTest() {

    private lateinit var sut: GetAllCardPairsUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetAllCardPairsUseCase(
            testDispatcher,
            CardRepository(
                MockAllCardPairsDataSource,
                UnlockedCardPairsLocalDataSource(
                    MockDataStore(),
                    MockAllCardPairsDataSource
                )
            )
        )
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
                            CardModel.Image("banana_whole", "banana", 1, 1) to
                                    CardModel.Image("banana_half", "banana", 1, 1)
                        ),
                        CardPairModel(
                            CardModel.Image("apple_whole", "apple", 2, 2) to
                                    CardModel.Text("apple_half", "apple", 2)
                        ),
                        CardPairModel(
                            CardModel.Text("strawberry_whole", "strawberry", 3) to
                                    CardModel.Text("strawberry_half", "strawberry", 3)
                        ),
                        CardPairModel(
                            CardModel.Text("orange_whole", "orange", 4) to
                                    CardModel.Text("orange_half", "orange", 4)
                        ),
                        CardPairModel(
                            CardModel.Text("grape_whole", "grape", 5) to
                                    CardModel.Text("grape_half", "grape", 5)
                        ),
                        CardPairModel(
                            CardModel.Text("watermelon_whole", "watermelon", 6) to
                                    CardModel.Text("watermelon_half", "watermelon", 6)
                        ),
                        CardPairModel(
                            CardModel.Text("mango_whole", "mango", 7) to
                                    CardModel.Text("mango_half", "mango", 7)
                        ),
                        CardPairModel(
                            CardModel.Text("peach_whole", "peach", 8) to
                                    CardModel.Text("peach_half", "peach", 8)
                        ),
                        CardPairModel(
                            CardModel.Text("pineapple_whole", "pineapple", 9) to
                                    CardModel.Text("pineapple_half", "pineapple", 9)
                        ),
                        CardPairModel(
                            CardModel.Text("blueberry_whole", "blueberry", 10) to
                                    CardModel.Text("blueberry_half", "blueberry", 10)
                        )
                    )
                )
                assertEquals(expected, awaitItem())
                awaitComplete()
            }
        }
}
