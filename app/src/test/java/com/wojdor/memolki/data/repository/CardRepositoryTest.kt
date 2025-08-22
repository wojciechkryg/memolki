package com.wojdor.memolki.data.repository

import com.wojdor.memolki.data.source.card.local.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockAllCardPairsDataSource
import com.wojdor.memolki.test.mock.MockSharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CardRepositoryTest : AppTest() {

    private val unlockedCardPairsLocalDataSource = UnlockedCardPairsLocalDataSource(
        MockSharedPreferences(),
        MockAllCardPairsDataSource
    )
    private lateinit var sut: CardRepository

    @Before
    override fun setup() {
        super.setup()
        sut = CardRepository(MockAllCardPairsDataSource, unlockedCardPairsLocalDataSource)
    }

    @Test
    fun `when data source returns card entities then repository returns mapped card pair models`() {
        // when
        val result = sut.getAllCardPairs()

        // then
        val expected = listOf(
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
        assertEquals(expected, result)
    }

    @Test
    fun `should return unlocked card pair ids`() {
        // given
        val expected = unlockedCardPairsLocalDataSource.getUnlockedCardPairIds()

        // when
        val result = sut.getUnlockedCardPairIds()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `should return random unlocked card pair ids`() {
        // given
        val count = 3

        // when
        val result = sut.getRandomUnlockedCardPairIds(count)

        // then
        assertEquals(count, result.size)
        assertTrue(unlockedCardPairsLocalDataSource.getUnlockedCardPairIds().containsAll(result))
    }

    @Test
    fun `should return card pair by id`() {
        // given
        val pairId = "banana"

        // when
        val result = sut.getCardPairById(pairId)

        // then
        val expected = CardPairModel(
            CardModel.Image("banana_whole", "banana", 1, 1) to
                    CardModel.Image("banana_half", "banana", 1, 1)
        )
        assertEquals(expected, result)
    }
}
