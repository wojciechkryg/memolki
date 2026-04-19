package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

@ExperimentalCoroutinesApi
class HasReceivedShareRewardUseCaseTest : AppTest() {

    private val userRepository: UserRepository by inject()

    private lateinit var sut: HasReceivedShareRewardUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = HasReceivedShareRewardUseCase(
            testDispatcher,
            userRepository
        )
    }

    @Test
    fun `when share reward not received then return false`() = runTest {
        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(false), result)
    }

    @Test
    fun `when share reward received then return true`() = runTest {
        // given
        userRepository.setHasReceivedShareReward()

        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(true), result)
    }
}
