package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.crypto.Encryptor
import com.wojdor.memolki.data.local.user.UserLocalDataSource
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockDataStore
import com.wojdor.memolki.test.mock.MockEncryptor
import com.wojdor.memolki.test.relaxedMockk
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class RewardCoinsForShopPurchaseUseCaseTest : AppTest() {

    private lateinit var userLocalDataSource: UserLocalDataSource
    private lateinit var userRepository: UserRepository
    private lateinit var sut: RewardCoinsForShopPurchaseUseCase

    override fun setup() {
        super.setup()
        userLocalDataSource = UserLocalDataSource(MockDataStore())
        userRepository = UserRepository(MockEncryptor(), userLocalDataSource)
        sut = RewardCoinsForShopPurchaseUseCase(testDispatcher, userRepository)
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
