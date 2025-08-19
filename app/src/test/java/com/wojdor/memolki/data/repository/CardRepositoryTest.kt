package com.wojdor.memolki.data.repository

import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockCardPairsDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CardRepositoryTest : AppTest() {

    private lateinit var sut: CardRepository

    @Before
    override fun setup() {
        super.setup()
        sut = CardRepository(MockCardPairsDataSource())
    }

    @Test
    fun `when data source returns card entities then repository returns mapped card pair models`() =
        runTest {
            // when
            val result = sut.getAllCardPairs()

            // then
            val expected = setOf(
                CardPairModel(
                    CardModel.Image("banana", 1, 1) to
                            CardModel.Image("banana", 1, 1)
                ),
                CardPairModel(
                    CardModel.Image("apple", 2, 2) to
                            CardModel.Text("apple", 2)
                ),
                CardPairModel(
                    CardModel.Text("strawberry", 3) to
                            CardModel.Text("strawberry", 3)
                ),
                CardPairModel(
                    CardModel.Text("orange", 4) to
                            CardModel.Text("orange", 4)
                ),
                CardPairModel(
                    CardModel.Text("grape", 5) to
                            CardModel.Text("grape", 5)
                ),
                CardPairModel(
                    CardModel.Text("watermelon", 6) to
                            CardModel.Text("watermelon", 6)
                ),
                CardPairModel(
                    CardModel.Text("mango", 7) to
                            CardModel.Text("mango", 7)
                ),
                CardPairModel(
                    CardModel.Text("peach", 8) to
                            CardModel.Text("peach", 8)
                ),
                CardPairModel(
                    CardModel.Text("pineapple", 9) to
                            CardModel.Text("pineapple", 9)
                ),
                CardPairModel(
                    CardModel.Text("blueberry", 10) to
                            CardModel.Text("blueberry", 10)
                )
            )
            assertEquals(expected, result)
        }
}
