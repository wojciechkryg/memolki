package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.local.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.data.local.user.UserLocalDataSource
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockAllCardPairsDataSource
import com.wojdor.memolki.test.mock.MockDataStore
import com.wojdor.memolki.test.mock.MockEncryptor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RewardCoinsForShopAdUseCaseTest : AppTest() {

    private lateinit var userRepository: UserRepository
    private lateinit var sut: RewardCoinsForShopAdUseCase

    @Before
    override fun setup() {
        super.setup()
        userRepository = UserRepository(
            MockEncryptor(), UserLocalDataSource(
                MockDataStore()
            )
        )
        sut = RewardCoinsForShopAdUseCase(
            testDispatcher,
            userRepository,
            GetLevelsUseCase(
                testDispatcher, GetUnlockedCardPairsCountUseCase(
                    testDispatcher,
                    CardRepository(
                        MockAllCardPairsDataSource, UnlockedCardPairsLocalDataSource(
                            MockDataStore(),
                            MockAllCardPairsDataSource
                        )
                    )
                )
            )
        )
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
