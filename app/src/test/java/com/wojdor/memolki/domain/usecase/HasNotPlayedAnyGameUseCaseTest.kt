package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class HasNotPlayedAnyGameUseCaseTest : AppTest() {

    @Inject
    lateinit var userRepository: UserRepository

    private lateinit var sut: HasNotPlayedAnyGameUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = HasNotPlayedAnyGameUseCase(
            testDispatcher,
            userRepository
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
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
            // initial emission: no games played
            assertEquals(Result.success(true), awaitItem())

            // given
            userRepository.incrementTotalGamesPlayed()

            // then
            assertEquals(Result.success(false), awaitItem())

            // given — further increments should not re-emit (distinctUntilChanged)
            userRepository.incrementTotalGamesPlayed()
            userRepository.incrementTotalGamesPlayed()

            // then
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }
}
