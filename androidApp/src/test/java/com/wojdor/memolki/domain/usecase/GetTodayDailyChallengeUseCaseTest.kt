package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeEntity
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.fake.FakeDailyChallengeDao
import com.wojdor.memolki.test.fake.FakeTimeProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.datetime.LocalDate
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class GetTodayDailyChallengeUseCaseTest : AppTest() {

    private val dailyChallengeDao: FakeDailyChallengeDao by inject()

    private val fakeTimeProvider: FakeTimeProvider by inject()

    private lateinit var sut: GetTodayDailyChallengeUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when entry exists for today then return model`() = runTest {
        // given
        val today = LocalDate(2026, 3, 26)
        fakeTimeProvider.mockCurrentDate = today
        val epochDay = today.toEpochDays()
        dailyChallengeDao.insertResult(
            DailyChallengeEntity(
                epochDay = epochDay,
                mistakeCount = 3,
                starCount = 2,
                timeMillis = 45000L,
                cardFlipCounts = "1,2;3,4"
            )
        )

        // when / then
        val expected = DailyChallengeModel(
            epochDay = epochDay,
            mistakeCount = 3,
            starCount = 2,
            timeMillis = 45000L,
            cardFlipCounts = listOf(listOf(1, 2), listOf(3, 4))
        )
        sut().test {
            assertEquals(Result.success(expected), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `when no entry exists for today then return failure`() = runTest {
        // given
        val today = LocalDate(2026, 3, 26)
        fakeTimeProvider.mockCurrentDate = today

        // when / then
        sut().test {
            val result = awaitItem()
            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is IllegalStateException)
            awaitComplete()
        }
    }
}
