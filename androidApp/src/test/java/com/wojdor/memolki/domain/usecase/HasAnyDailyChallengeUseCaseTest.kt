package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeDao
import com.wojdor.memolki.data.repository.DailyChallengeRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.relaxedMockk
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class HasAnyDailyChallengeUseCaseTest : AppTest() {

    private val dailyChallengeDao: DailyChallengeDao by inject()

    private lateinit var sut: HasAnyDailyChallengeUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when entries exist then return true`() = runTest {
        // given
        coEvery { dailyChallengeDao.hasAnyCompleted() } returns true

        // when
        sut().test {
            // then
            assertTrue(awaitItem().getOrThrow())
            awaitComplete()
        }
    }

    @Test
    fun `when no entries exist then return false`() = runTest {
        // given
        coEvery { dailyChallengeDao.hasAnyCompleted() } returns false

        // when
        sut().test {
            // then
            assertFalse(awaitItem().getOrThrow())
            awaitComplete()
        }
    }
}
