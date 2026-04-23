package com.wojdor.memolki.data.repository

import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.watermelon
import com.wojdor.memolki.shared.resources.strawberry
import com.wojdor.memolki.shared.resources.pineapple
import com.wojdor.memolki.shared.resources.peach
import com.wojdor.memolki.shared.resources.orange
import com.wojdor.memolki.shared.resources.mango
import com.wojdor.memolki.shared.resources.grape
import com.wojdor.memolki.shared.resources.blueberry
import com.wojdor.memolki.shared.resources.banana
import com.wojdor.memolki.shared.resources.apple
import com.wojdor.memolki.shared.resources.empty

import com.wojdor.memolki.data.local.datastore.card.AllCardPairsDataSource
import com.wojdor.memolki.data.local.datastore.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.data.mapper.toModel
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.fake.FakeAllCardPairsDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.random.Random
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class CardRepositoryTest : AppTest() {

    private val allCardPairsDataSource: AllCardPairsDataSource by inject()

    private val unlockedCardPairsLocalDataSource: UnlockedCardPairsLocalDataSource by inject()

    private val random: Random by inject()

    private lateinit var sut: CardRepository

    @Before
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when data source returns card entities then repository returns mapped card pair models`() {
        // when
        val result = sut.getAllCardPairs()

        // then
        val expected = listOf(
            CardPairModel(
                CardModel.Image("banana_whole", "banana", Res.string.banana, 1),
                CardModel.Image("banana_half", "banana", Res.string.banana, 1)
            ),
            CardPairModel(
                CardModel.Image("apple_whole", "apple", Res.string.apple, 2),
                CardModel.Text("apple_half", "apple", Res.string.apple)
            ),
            CardPairModel(
                CardModel.Text("strawberry_whole", "strawberry", Res.string.strawberry),
                CardModel.Text("strawberry_half", "strawberry", Res.string.strawberry)
            ),
            CardPairModel(
                CardModel.Text("orange_whole", "orange", Res.string.orange),
                CardModel.Text("orange_half", "orange", Res.string.orange)
            ),
            CardPairModel(
                CardModel.Text("grape_whole", "grape", Res.string.grape),
                CardModel.Text("grape_half", "grape", Res.string.grape)
            ),
            CardPairModel(
                CardModel.Text("watermelon_whole", "watermelon", Res.string.watermelon),
                CardModel.Text("watermelon_half", "watermelon", Res.string.watermelon)
            ),
            CardPairModel(
                CardModel.Text("mango_whole", "mango", Res.string.mango),
                CardModel.Text("mango_half", "mango", Res.string.mango)
            ),
            CardPairModel(
                CardModel.Text("peach_whole", "peach", Res.string.peach),
                CardModel.Text("peach_half", "peach", Res.string.peach)
            ),
            CardPairModel(
                CardModel.Text("pineapple_whole", "pineapple", Res.string.pineapple),
                CardModel.Text("pineapple_half", "pineapple", Res.string.pineapple)
            ),
            CardPairModel(
                CardModel.Text("blueberry_whole", "blueberry", Res.string.blueberry),
                CardModel.Text("blueberry_half", "blueberry", Res.string.blueberry)
            )
        )
        assertEquals(expected, result)
    }

    @Test
    fun `when getUnlockedCardPairs called then return only unlocked cards `() = runTest {
        // given
        val allCardPairs = sut.getAllCardPairs()

        // when
        val actualUnlockedCardPairs = sut.getUnlockedCardPairs()

        // then
        val expected = FakeAllCardPairsDataSource().getAllCardPairs()
            .take(5)
            .toModel()
        assertEquals(expected, actualUnlockedCardPairs)
    }

    @Test
    fun `should return random unlocked card pair ids`() = runTest {
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
            CardModel.Image("banana_whole", "banana", Res.string.banana, 1),
            CardModel.Image("banana_half", "banana", Res.string.banana, 1)
        )
        assertEquals(expected, result)
    }

    @Test
    fun `when getCardPairById with unknown id then return null`() {
        // when
        val result = sut.getCardPairById("does-not-exist")

        // then
        assertEquals(null, result)
    }

    @Test
    fun `when unlocked id is not in all pairs then it is skipped`() = runTest {
        // given
        val baseline = sut.getUnlockedCardPairs()
        unlockedCardPairsLocalDataSource.addUnlockedCardPairId("does-not-exist")

        // when
        val result = sut.getUnlockedCardPairs()

        // then
        assertEquals(baseline, result)
    }
}
