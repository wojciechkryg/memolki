package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class CanUnlockNewCardUseCaseTest : AppTest() {

    @Inject
    lateinit var getCoinsUseCase: GetCoinsUseCase

    @Inject
    lateinit var calculateNextCardPairCostUseCase: CalculateNextCardPairCostUseCase

    @Inject
    lateinit var userRepository: UserRepository

    private lateinit var sut: CanUnlockNewCardUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = CanUnlockNewCardUseCase(
            testDispatcher,
            getCoinsUseCase,
            calculateNextCardPairCostUseCase
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when user does not have enough coins then return false`() = runTest {
        // when
        val result = sut().first()

        // then
        assertFalse(result.getOrThrow())
    }

    @Test
    fun `when user has enough coins then return true`() = runTest {
        // given
        userRepository.addCoins(11L)

        // when
        val result = sut().first()

        // then
        assertTrue(result.getOrThrow())
    }
}
