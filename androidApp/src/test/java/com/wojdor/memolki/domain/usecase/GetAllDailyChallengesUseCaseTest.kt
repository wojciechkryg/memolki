package com.wojdor.memolki.domain.usecase

import android.util.Log
import app.cash.turbine.test
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeDao
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeEntity
import com.wojdor.memolki.data.repository.DailyChallengeRepository
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.relaxedMockk
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

@ExperimentalCoroutinesApi
class GetAllDailyChallengesUseCaseTest : AppTest() {

    private val dailyChallengeDao: DailyChallengeDao by inject()

    private lateinit var sut: GetAllDailyChallengesUseCase

    @Before
    override fun setup() {
        super.setup()
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        mockkStatic(FirebaseCrashlytics::class)
        every { FirebaseCrashlytics.getInstance() } returns relaxedMockk()
        sut = GetAllDailyChallengesUseCase(
            testDispatcher,
            DailyChallengeRepository(dailyChallengeDao)
        )
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
