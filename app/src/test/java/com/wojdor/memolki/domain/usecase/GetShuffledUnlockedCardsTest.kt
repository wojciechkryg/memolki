package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.data.source.card.local.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockAllCardPairsDataSource
import com.wojdor.memolki.test.mock.MockSharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetShuffledUnlockedCardsTest : AppTest() {

    private lateinit var sut: GetShuffledUnlockedCards

    @Before
    override fun setup() {
        super.setup()
        sut = GetShuffledUnlockedCards(
            testDispatcher,
            CardRepository(
                MockAllCardPairsDataSource, UnlockedCardPairsLocalDataSource(
                    MockSharedPreferences(), MockAllCardPairsDataSource
                )
            )
        )
    }

    @Test
    fun `when called then returns shuffled unlocked card pair ids`() = runTest {
        // given
        val level = LevelModel.Grid2x3

        // when
        sut(level).test {
            // then
            val result = awaitItem().getOrElse { listOf() }
            val notExpected = Result.success(
                listOf(
                    listOf(
                        "banana",
                        "apple"
                    ),
                    listOf(
                        "strawberry",
                        "orange"
                    ),
                    listOf(
                        "grape",
                        "watermelon"
                    )
                )
            )
            assertEquals(3, result.size)
            assertNotEquals(notExpected.getOrNull(), result)
            assertTrue(result.all { it.size == 2 })
            awaitComplete()
        }
    }
}
