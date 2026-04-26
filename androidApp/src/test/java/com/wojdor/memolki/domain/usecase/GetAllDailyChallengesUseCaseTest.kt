package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeDao
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeEntity
import com.wojdor.memolki.data.repository.DailyChallengeRepository
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.relaxedMockk
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class GetAllDailyChallengesUseCaseTest : AppTest() {

    private val dailyChallengeDao: DailyChallengeDao by inject()

    private lateinit var sut: GetAllDailyChallengesUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when entries exist then return all models`() = runTest {
        // given
        val entities = listOf(
            DailyChallengeEntity(
                epochDay = 20001L,
                mistakeCount = 0,
                starCount = 3,
                timeMillis = 45000L,
                cardFlipCounts = "2,2;2,2"
            ),
            DailyChallengeEntity(
                epochDay = 20000L,
                mistakeCount = 3,
                starCount = 2,
                timeMillis = 60000L,
                cardFlipCounts = "2,3;4,2"
            )
        )
        coEvery { dailyChallengeDao.getAll() } returns entities

        val expected = listOf(
            DailyChallengeModel(
                epochDay = 20001L,
                mistakeCount = 0,
                starCount = 3,
                timeMillis = 45000L,
                cardFlipCounts = listOf(listOf(2, 2), listOf(2, 2))
            ),
            DailyChallengeModel(
                epochDay = 20000L,
                mistakeCount = 3,
                starCount = 2,
                timeMillis = 60000L,
                cardFlipCounts = listOf(listOf(2, 3), listOf(4, 2))
            )
        )

        // when
        sut().test {
            // then
            assertEquals(Result.success(expected), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `when no entries exist then return empty list`() = runTest {
        // given
        coEvery { dailyChallengeDao.getAll() } returns emptyList()

        // when
        sut().test {
            // then
            assertEquals(Result.success(emptyList<DailyChallengeModel>()), awaitItem())
            awaitComplete()
        }
    }
}
