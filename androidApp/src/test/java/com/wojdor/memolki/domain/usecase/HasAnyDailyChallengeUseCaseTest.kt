package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.dailyChallengeEntity
import com.wojdor.memolki.test.fake.FakeDailyChallengeDao
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

    private val dailyChallengeDao: FakeDailyChallengeDao by inject()

    private lateinit var sut: HasAnyDailyChallengeUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when entries exist then return true`() = runTest {
        // given
        dailyChallengeDao.insertResult(dailyChallengeEntity(starCount = 3))

        // when
        sut().test {
            // then
            assertTrue(awaitItem().getOrThrow())
            awaitComplete()
        }
    }

    @Test
    fun `when no entries exist then return false`() = runTest {
        // when
        sut().test {
            // then
            assertFalse(awaitItem().getOrThrow())
            awaitComplete()
        }
    }

}
