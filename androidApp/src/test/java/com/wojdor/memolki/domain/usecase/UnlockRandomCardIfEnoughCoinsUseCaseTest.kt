package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.local.datastore.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class UnlockRandomCardIfEnoughCoinsUseCaseTest : AppTest() {

    private val calculateNextCardPairCostUseCase: CalculateNextCardPairCostUseCase by inject()

    private val unlockRandomCardUseCase: UnlockRandomCardUseCase by inject()

    private val userRepository: UserRepository by inject()

    private val unlockedCardPairsLocalDataSource: UnlockedCardPairsLocalDataSource by inject()

    private lateinit var sut: UnlockRandomCardIfEnoughCoinsUseCase

    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when user has not enough coins then should emit failure`() = runTest {
        // given
        val nextCardCost = calculateNextCardPairCostUseCase().first().getOrThrow()
        userRepository.addCoins(nextCardCost - 1L)

        // when
        sut().test {
            // then
            val result = awaitItem()
            assertTrue(result.isFailure)
            awaitComplete()
        }
    }

    @Test
    fun `when user has enough coins then should unlock a random card and emit success`() = runTest {
        // given
        val nextCardCost = calculateNextCardPairCostUseCase().first().getOrThrow()
        userRepository.addCoins(nextCardCost.toLong())
        val unlockedCardPairsBefore = unlockedCardPairsLocalDataSource.getUnlockedCardPairIds().size

        // when
        sut().test {
            // then
            val result = awaitItem()
            assertTrue(result.isSuccess)
            awaitComplete()
        }

        // and then
        val unlockedCardPairsAfter = unlockedCardPairsLocalDataSource.getUnlockedCardPairIds().size
        assertEquals(unlockedCardPairsBefore + 1, unlockedCardPairsAfter)
    }

    @Test
    fun `when user has more than enough coins then should remove correct amount of coins`() =
        runTest {
            // given
            val extraCoins = 100L
            val nextCardCost = calculateNextCardPairCostUseCase().first().getOrThrow()
            userRepository.addCoins(nextCardCost + extraCoins)

            // when
            sut().test {
                awaitItem()
                awaitComplete()
            }

            // then
            assertEquals(extraCoins, userRepository.getCoins().first())
        }
}
