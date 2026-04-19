package com.wojdor.memolki.domain.usecase

import android.util.Log
import app.cash.turbine.test
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeDao
import com.wojdor.memolki.data.repository.DailyChallengeRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.test.relaxedMockk
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class HasAnyDailyChallengeUseCaseTest : AppTest() {

    @Inject
    lateinit var dailyChallengeDao: DailyChallengeDao

    private lateinit var sut: HasAnyDailyChallengeUseCase

    @Before
    override fun setup() {
        super.setup()
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        mockkStatic(FirebaseCrashlytics::class)
        every { FirebaseCrashlytics.getInstance() } returns relaxedMockk()
        sut = HasAnyDailyChallengeUseCase(
            testDispatcher,
            DailyChallengeRepository(dailyChallengeDao)
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
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
