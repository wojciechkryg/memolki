package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.test.fake.FakeTimeProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetDailyChallengeCardsUseCaseTest : AppTest() {

    @Inject
    lateinit var cardRepository: CardRepository

    @Inject
    lateinit var fakeTimeProvider: FakeTimeProvider

    private lateinit var sut: GetDailyChallengeCardsUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetDailyChallengeCardsUseCase(
            testDispatcher,
            cardRepository,
            fakeTimeProvider
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when get cards for level then return correct number of cards`() = runTest {
        // given
        val level = LevelModel.Grid2x3()
        fakeTimeProvider.mockCurrentDate = LocalDate.of(2026, 3, 26)

        // when
        val result = sut(level).first()

        // then
        val expectedCardCount = level.columns * level.rows
        assertEquals(expectedCardCount, result.getOrThrow().size)
    }

    @Test
    fun `when get cards for larger level then return correct number of cards`() = runTest {
        // given
        val level = LevelModel.Grid4x4()
        fakeTimeProvider.mockCurrentDate = LocalDate.of(2026, 3, 26)

        // when
        val result = sut(level).first()

        // then
        val expectedCardCount = level.columns * level.rows
        assertEquals(expectedCardCount, result.getOrThrow().size)
    }

    @Test
    fun `when same date then return same cards`() = runTest {
        // given
        val level = LevelModel.Grid2x3()
        fakeTimeProvider.mockCurrentDate = LocalDate.of(2026, 3, 26)

        // when
        val result1 = sut(level).first().getOrThrow()
        val result2 = sut(level).first().getOrThrow()

        // then
        assertEquals(result1, result2)
    }

    @Test
    fun `when different date then return different cards`() = runTest {
        // given
        val level = LevelModel.Grid2x3()

        // when
        fakeTimeProvider.mockCurrentDate = LocalDate.of(2026, 3, 26)
        val result1 = sut(level).first().getOrThrow()
        fakeTimeProvider.mockCurrentDate = LocalDate.of(2026, 3, 27)
        val result2 = sut(level).first().getOrThrow()

        // then
        assertNotEquals(result1, result2)
    }
}
