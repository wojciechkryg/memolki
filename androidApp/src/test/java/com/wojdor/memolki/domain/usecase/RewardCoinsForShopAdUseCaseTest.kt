package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class RewardCoinsForShopAdUseCaseTest : AppTest() {

    private val userRepository: UserRepository by inject()

    private val calculateCoinsForShopAdUseCase: CalculateCoinsForShopAdUseCase by inject()

    private lateinit var sut: RewardCoinsForShopAdUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `given unlocked levels when execute then should reward coins based on the biggest unlocked level`() =
        runTest {
            // when
            sut().test {
                assertEquals(Result.success(Unit), awaitItem())
                awaitComplete()

                // then
                assertEquals(6, userRepository.getCoins().first())
            }
        }
}
