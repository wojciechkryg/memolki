package com.wojdor.memolki.domain.usecase

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
class GetTotalCoinsUseCaseTest : AppTest() {

    @Inject
    lateinit var userRepository: UserRepository

    private lateinit var sut: GetTotalCoinsUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetTotalCoinsUseCase(
            testDispatcher,
            userRepository
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when no coins earned then return 0`() = runTest {
        // when
        val result = sut().first()

        // then
        val expected = Result.success(0L)
        assertEquals(expected, result)
    }

    @Test
    fun `when coins are added then return total coins`() = runTest {
        // given
        userRepository.addCoins(50)

        // when
        val result = sut().first()

        // then
        val expected = Result.success(50L)
        assertEquals(expected, result)
    }
}
