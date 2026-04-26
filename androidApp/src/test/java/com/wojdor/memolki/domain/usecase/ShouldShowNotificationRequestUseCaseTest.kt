package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.fake.FakeNotificationScheduler
import com.wojdor.memolki.test.fake.FakePermissionProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class ShouldShowNotificationRequestUseCaseTest : AppTest() {

    private val userRepository: UserRepository by inject()

    private val fakeNotificationScheduler: FakeNotificationScheduler by inject()

    private val fakePermissionProvider: FakePermissionProvider by inject()

    private lateinit var sut: ShouldShowNotificationRequestUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when permission already granted then return false`() = runTest {
        // given
        fakePermissionProvider.hasPermission = true

        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(false), result)
    }

    @Test
    fun `when first game played then return true`() = runTest {
        // given
        fakePermissionProvider.hasPermission = false
        userRepository.incrementTotalGamesPlayed()

        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(true), result)
    }

    @Test
    fun `when no games played then return false`() = runTest {
        // given
        fakePermissionProvider.hasPermission = false

        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(false), result)
    }

    @Test
    fun `when 4 games played then return true`() = runTest {
        // given
        fakePermissionProvider.hasPermission = false
        repeat(4) { userRepository.incrementTotalGamesPlayed() }

        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(true), result)
    }

    @Test
    fun `when 2 games played then return false`() = runTest {
        // given
        fakePermissionProvider.hasPermission = false
        repeat(2) { userRepository.incrementTotalGamesPlayed() }

        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(false), result)
    }
}
