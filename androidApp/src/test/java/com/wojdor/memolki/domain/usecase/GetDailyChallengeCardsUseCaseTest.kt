package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.fake.FakeAllCardPairsDataSource
import com.wojdor.memolki.test.fake.FakePackageNameProvider
import com.wojdor.memolki.test.fake.FakeTimeProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class GetDailyChallengeCardsUseCaseTest : AppTest() {

    private val fakeAllCardPairsDataSource: FakeAllCardPairsDataSource by inject()
    private val fakeTimeProvider: FakeTimeProvider by inject()
    private val fakePackageNameProvider: FakePackageNameProvider by inject()

    private lateinit var sut: GetDailyChallengeCardsUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when get cards for level then return correct number of cards`() = runTest {
        // given
        val board = BoardModel.Grid2x3()
        fakeTimeProvider.mockCurrentDate = LocalDate(2026, 3, 26)

        // when / then
        val expectedCardCount = board.columns * board.rows
        sut(board).test {
            assertEquals(expectedCardCount, awaitItem().getOrThrow().size)
            awaitComplete()
        }
    }

    @Test
    fun `when get cards for larger level then return correct number of cards`() = runTest {
        // given
        val board = BoardModel.Grid4x4()
        fakeTimeProvider.mockCurrentDate = LocalDate(2026, 3, 26)

        // when / then
        val expectedCardCount = board.columns * board.rows
        sut(board).test {
            assertEquals(expectedCardCount, awaitItem().getOrThrow().size)
            awaitComplete()
        }
    }

    @Test
    fun `when same date then return same cards`() = runTest {
        // given
        val board = BoardModel.Grid2x3()
        fakeTimeProvider.mockCurrentDate = LocalDate(2026, 3, 26)

        // when
        var result1: List<*>? = null
        sut(board).test {
            result1 = awaitItem().getOrThrow()
            awaitComplete()
        }
        var result2: List<*>? = null
        sut(board).test {
            result2 = awaitItem().getOrThrow()
            awaitComplete()
        }

        // then
        assertEquals(result1, result2)
    }

    @Test
    fun `when different date then return different cards`() = runTest {
        // given
        val board = BoardModel.Grid2x3()

        // when
        fakeTimeProvider.mockCurrentDate = LocalDate(2026, 3, 26)
        var result1: List<*>? = null
        sut(board).test {
            result1 = awaitItem().getOrThrow()
            awaitComplete()
        }
        fakeTimeProvider.mockCurrentDate = LocalDate(2026, 3, 27)
        var result2: List<*>? = null
        sut(board).test {
            result2 = awaitItem().getOrThrow()
            awaitComplete()
        }

        // then
        assertNotEquals(result1, result2)
    }

    @Test
    fun `when different flavor on same date then return different card order`() = runTest {
        // given
        val board = BoardModel.Grid2x3()
        fakeTimeProvider.mockCurrentDate = LocalDate(2026, 3, 26)

        // when
        fakePackageNameProvider.mockPackageName = "com.wojdor.memolki.fruithalf"
        var result1: List<*>? = null
        sut(board).test {
            result1 = awaitItem().getOrThrow()
            awaitComplete()
        }
        fakePackageNameProvider.mockPackageName = "com.wojdor.memolki.mammalside"
        var result2: List<*>? = null
        sut(board).test {
            result2 = awaitItem().getOrThrow()
            awaitComplete()
        }

        // then
        assertNotEquals(result1, result2)
    }

    @Test
    fun `when cards are within grace period then they are deprioritized`() = runTest {
        // given
        val board = BoardModel.Grid2x3()
        val testDate = LocalDate(2026, 3, 26)
        val testEpochDay = testDate.toEpochDays()
        fakeTimeProvider.mockCurrentDate = testDate
        fakeAllCardPairsDataSource.addedEpochDayOverrides = mapOf(
            "banana" to testEpochDay - 10,
            "apple" to testEpochDay - 10,
            "strawberry" to testEpochDay - 10
        )
        val pairCount = (board.columns * board.rows) / 2

        // when
        sut(board).test {
            val cards = awaitItem().getOrThrow()
            val selectedPairIds = cards.map { it.pairId }.distinct()

            // then
            val gracePeriodIds = setOf("banana", "apple", "strawberry")
            val nonGracePeriodSelected = selectedPairIds.count { it !in gracePeriodIds }
            assertEquals(pairCount, nonGracePeriodSelected)
            awaitComplete()
        }
    }
}
