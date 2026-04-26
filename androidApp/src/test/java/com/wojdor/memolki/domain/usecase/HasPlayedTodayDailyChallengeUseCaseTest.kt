package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.dailyChallengeEntity
import com.wojdor.memolki.test.fake.FakeDailyChallengeDao
import com.wojdor.memolki.test.fake.FakeTimeProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.datetime.LocalDate
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class HasPlayedTodayDailyChallengeUseCaseTest : AppTest() {

    private val dailyChallengeDao: FakeDailyChallengeDao by inject()

    private val fakeTimeProvider: FakeTimeProvider by inject()

    private lateinit var sut: HasPlayedTodayDailyChallengeUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when played today then return true`() = runTest {
        // given
        val today = LocalDate(2026, 3, 26)
        fakeTimeProvider.mockCurrentDate = today
        dailyChallengeDao.insertResult(dailyChallengeEntity(epochDay = today.toEpochDays()))

        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(true), result)
    }

    @Test
    fun `when not played today then return false`() = runTest {
        // given
        val today = LocalDate(2026, 3, 26)
        fakeTimeProvider.mockCurrentDate = today

        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(false), result)
    }

    @Test
    fun `when date rolled back then return true`() = runTest {
        // given
        val today = LocalDate(2026, 3, 24)
        fakeTimeProvider.mockCurrentDate = today
        dailyChallengeDao.insertResult(dailyChallengeEntity(epochDay = LocalDate(2026, 3, 26).toEpochDays()))

        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(true), result)
    }

}
