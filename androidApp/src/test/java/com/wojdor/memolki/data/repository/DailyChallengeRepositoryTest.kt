package com.wojdor.memolki.data.repository

import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeEntity
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.dailyChallengeEntity
import com.wojdor.memolki.test.fake.FakeDailyChallengeDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class DailyChallengeRepositoryTest : AppTest() {

    private val dailyChallengeDao: FakeDailyChallengeDao by inject()

    private lateinit var sut: DailyChallengeRepository

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when has played for epoch day then return true`() = runTest {
        // given
        val epochDay = 20000L
        dailyChallengeDao.insertResult(dailyChallengeEntity(epochDay = epochDay))

        // when
        val result = sut.hasPlayed(epochDay)

        // then
        assertTrue(result)
    }

    @Test
    fun `when has not played for epoch day then return false`() = runTest {
        // given
        val epochDay = 20000L

        // when
        val result = sut.hasPlayed(epochDay)

        // then
        assertFalse(result)
    }

    @Test
    fun `when get result for epoch day with entry then return model`() = runTest {
        // given
        val epochDay = 20000L
        dailyChallengeDao.insertResult(
            DailyChallengeEntity(
                epochDay = epochDay,
                mistakeCount = 3,
                starCount = 2,
                timeMillis = 45000L,
                cardFlipCounts = "1,2;3,4"
            )
        )

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
        // when
        val result = sut.getResult(20000L)

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
        assertEquals(expectedEntity, dailyChallengeDao.getResult(epochDay))
    }

    @Test
    fun `when get last played epoch day with entries then return max epoch day`() = runTest {
        // given
        dailyChallengeDao.insertResult(dailyChallengeEntity(epochDay = 20003L))
        dailyChallengeDao.insertResult(dailyChallengeEntity(epochDay = 20005L))
        dailyChallengeDao.insertResult(dailyChallengeEntity(epochDay = 20001L))

        // when
        val result = sut.getLastPlayedEpochDay()

        // then
        assertEquals(20005L, result)
    }

    @Test
    fun `when get last played epoch day without entries then return null`() = runTest {
        // when
        val result = sut.getLastPlayedEpochDay()

        // then
        assertNull(result)
    }

    @Test
    fun `when get all results with entries then return mapped models`() = runTest {
        // given
        dailyChallengeDao.insertResult(
            DailyChallengeEntity(
                epochDay = 20001L,
                mistakeCount = 0,
                starCount = 3,
                timeMillis = 45000L,
                cardFlipCounts = "2,2;2,2"
            )
        )
        dailyChallengeDao.insertResult(
            DailyChallengeEntity(
                epochDay = 20000L,
                mistakeCount = 3,
                starCount = 2,
                timeMillis = 60000L,
                cardFlipCounts = "2,3;4,2"
            )
        )

        // when
        val result = sut.getAll()

        // then
        assertEquals(2, result.size)
        assertEquals(20001L, result[0].epochDay)
        assertEquals(20000L, result[1].epochDay)
    }

    @Test
    fun `when get all results without entries then return empty list`() = runTest {
        // when
        val result = sut.getAll()

        // then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `when has any result with entries then return true`() = runTest {
        // given
        dailyChallengeDao.insertResult(dailyChallengeEntity(epochDay = 20000L))

        // when
        val result = sut.hasAnyCompleted()

        // then
        assertTrue(result)
    }

    @Test
    fun `when has any result without entries then return false`() = runTest {
        // when
        val result = sut.hasAnyCompleted()

        // then
        assertFalse(result)
    }

}
