package com.wojdor.memolki.data.repository

import com.wojdor.memolki.AppTest
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.mockCardPairsLocalDataSource
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
        sut = CardRepository(mockCardPairsLocalDataSource)
    }

    @Test
    fun `when data source returns card entities then repository returns mapped card pair models`() =
        runTest {
            // when
            val result = sut.getCardPairs()

            // then
            val expected = listOf(
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
            assertEquals(expected, result)
        }
}
