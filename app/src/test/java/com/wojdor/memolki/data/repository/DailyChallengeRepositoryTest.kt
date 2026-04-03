package com.wojdor.memolki.data.repository

import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeDao
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeEntity
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.coVerifyOnce
import com.wojdor.memolki.test.di.TestInjector
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class DailyChallengeRepositoryTest : AppTest() {

    @Inject
    lateinit var dailyChallengeDao: DailyChallengeDao

    private lateinit var sut: DailyChallengeRepository

    @Before
    override fun setup() {
        super.setup()
        sut = DailyChallengeRepository(dailyChallengeDao)
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when has played for epoch day then return true`() = runTest {
        // given
        val epochDay = 20000L
        coEvery { dailyChallengeDao.hasPlayed(epochDay) } returns true

        // when
        val result = sut.hasPlayed(epochDay)

        // then
        assertTrue(result)
    }

    @Test
    fun `when has not played for epoch day then return false`() = runTest {
        // given
        val epochDay = 20000L
        coEvery { dailyChallengeDao.hasPlayed(epochDay) } returns false

        // when
        val result = sut.hasPlayed(epochDay)

        // then
        assertFalse(result)
    }

    @Test
    fun `when get result for epoch day with entry then return model`() = runTest {
        // given
        val epochDay = 20000L
        val entity = DailyChallengeEntity(
            epochDay = epochDay,
            mistakeCount = 3,
            starCount = 2,
            timeMillis = 45000L,
            cardFlipCounts = "1,2;3,4"
        )
        coEvery { dailyChallengeDao.getResult(epochDay) } returns entity

        // when
        val result = sut.getResult(epochDay)

        // then
        val expected = DailyChallengeModel(
            epochDay = epochDay,
            mistakeCount = 3,
            starCount = 2,
            timeMillis = 45000L,
            cardFlipCounts = listOf(listOf(1, 2), listOf(3, 4))
        )
        assertEquals(expected, result)
    }

    @Test
    fun `when get result for epoch day without entry then return null`() = runTest {
        // given
        val epochDay = 20000L
        coEvery { dailyChallengeDao.getResult(epochDay) } returns null

        // when
        val result = sut.getResult(epochDay)

        // then
        assertNull(result)
    }

    @Test
    fun `when save result then insert entity into dao`() = runTest {
        // given
        val epochDay = 20000L
        val model = DailyChallengeModel(
            epochDay = epochDay,
            mistakeCount = 3,
            starCount = 2,
            timeMillis = 45000L,
            cardFlipCounts = listOf(listOf(1, 2), listOf(3, 4))
        )

        // when
        sut.saveResult(epochDay, model)

        // then
        val expectedEntity = DailyChallengeEntity(
            epochDay = epochDay,
            mistakeCount = 3,
            starCount = 2,
            timeMillis = 45000L,
            cardFlipCounts = "1,2;3,4"
        )
        coVerifyOnce { dailyChallengeDao.insertResult(expectedEntity) }
    }

    @Test
    fun `when get last played epoch day with entries then return max epoch day`() = runTest {
        // given
        val lastPlayedEpochDay = 20005L
        coEvery { dailyChallengeDao.getLastPlayedEpochDay() } returns lastPlayedEpochDay

        // when
        val result = sut.getLastPlayedEpochDay()

        // then
        assertEquals(lastPlayedEpochDay, result)
    }

    @Test
    fun `when get last played epoch day without entries then return null`() = runTest {
        // given
        coEvery { dailyChallengeDao.getLastPlayedEpochDay() } returns null

        // when
        val result = sut.getLastPlayedEpochDay()

        // then
        assertNull(result)
    }
}
