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
class IsShopAdCooldownOverUseCaseTest : AppTest() {

    private val userRepository: UserRepository by inject()

    private lateinit var sut: IsShopAdCooldownOverUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = IsShopAdCooldownOverUseCase(
            testDispatcher,
            userRepository
        )
    }

    @Test
    fun `when no timestamp stored then cooldown is over`() = runTest {
        // when
        val result = sut().first()

        // then
        val expected = Result.success(true)
        assertEquals(expected, result)
    }

    @Test
    fun `when timestamp is recent then cooldown is not over`() = runTest {
        // given
        userRepository.setLastShopAdShownTimestamp(System.currentTimeMillis())

        // when
        val result = sut().first()

        // then
        val expected = Result.success(false)
        assertEquals(expected, result)
    }
}
