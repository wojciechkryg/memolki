package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class RewardCoinsForShopPurchaseUseCaseTest : AppTest() {

    @Inject
    lateinit var userRepository: UserRepository

    private lateinit var sut: RewardCoinsForShopPurchaseUseCase

    override fun setup() {
        super.setup()
        sut = RewardCoinsForShopPurchaseUseCase(
            testDispatcher,
            userRepository
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when user buys coins then add correct amount of coins`() = runTest {
        // given
        userRepository.addCoins(10)

        // when
        sut(100).first()

        // then
        assertEquals(110, userRepository.getCoins().first())
    }
}
