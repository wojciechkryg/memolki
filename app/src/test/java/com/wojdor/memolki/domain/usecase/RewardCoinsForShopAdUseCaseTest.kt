package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
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
class RewardCoinsForShopAdUseCaseTest : AppTest() {

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var getLevelUseCase: GetLevelsUseCase

    private lateinit var sut: RewardCoinsForShopAdUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = RewardCoinsForShopAdUseCase(
            testDispatcher,
            userRepository,
            getLevelUseCase
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
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
