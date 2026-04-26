package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class HasNotPlayedAnyGameUseCaseTest : AppTest() {

    private val userRepository: UserRepository by inject()

    private lateinit var sut: HasNotPlayedAnyGameUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when no games played then return true`() = runTest {
        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(true), result)
    }

    @Test
    fun `when one game played then return false`() = runTest {
        // given
        userRepository.incrementTotalGamesPlayed()

        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(false), result)
    }

    @Test
    fun `when several games played then return false`() = runTest {
        // given
        repeat(5) { userRepository.incrementTotalGamesPlayed() }

        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(false), result)
    }

    @Test
    fun `when games count transitions from zero to one then emit once per distinct value`() = runTest {
        sut().test {
            // then
            assertEquals(Result.success(true), awaitItem())

            // given
            userRepository.incrementTotalGamesPlayed()

            // then
            assertEquals(Result.success(false), awaitItem())

            // given
            userRepository.incrementTotalGamesPlayed()
            userRepository.incrementTotalGamesPlayed()

            // then
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }
}
