package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.test.fake.FakeNotificationScheduler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ShouldShowNotificationRequestUseCaseTest : AppTest() {

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var fakeNotificationScheduler: FakeNotificationScheduler

    private lateinit var sut: ShouldShowNotificationRequestUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = ShouldShowNotificationRequestUseCase(
            testDispatcher,
            userRepository,
            fakeNotificationScheduler
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when permission already granted then return false`() = runTest {
        // given
        fakeNotificationScheduler.hasPermission = true

        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(false), result)
    }

    @Test
    fun `when first game played then return true`() = runTest {
        // given
        fakeNotificationScheduler.hasPermission = false
        userRepository.incrementTotalGamesPlayed()

        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(true), result)
    }

    @Test
    fun `when no games played then return false`() = runTest {
        // given
        fakeNotificationScheduler.hasPermission = false

        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(false), result)
    }

    @Test
    fun `when 4 games played then return true`() = runTest {
        // given
        fakeNotificationScheduler.hasPermission = false
        repeat(4) { userRepository.incrementTotalGamesPlayed() }

        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(true), result)
    }

    @Test
    fun `when 2 games played then return false`() = runTest {
        // given
        fakeNotificationScheduler.hasPermission = false
        repeat(2) { userRepository.incrementTotalGamesPlayed() }

        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(false), result)
    }
}
