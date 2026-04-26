package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class RewardCoinsForShopPurchaseUseCaseTest : AppTest() {

    private val userRepository: UserRepository by inject()

    private lateinit var sut: RewardCoinsForShopPurchaseUseCase

    override fun setup() {
        super.setup()
        sut = get()
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
