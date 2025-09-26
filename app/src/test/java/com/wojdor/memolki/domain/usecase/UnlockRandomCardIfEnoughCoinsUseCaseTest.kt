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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.NoSuchElementException

@ExperimentalCoroutinesApi
class UnlockRandomCardIfEnoughCoinsUseCaseTest : AppTest() {

    private lateinit var unlockedCardPairsLocalDataSource: UnlockedCardPairsLocalDataSource
    private lateinit var cardRepository: CardRepository
    private lateinit var userRepository: UserRepository
    private lateinit var calculateNextCardPairCostUseCase: CalculateNextCardPairCostUseCase
    private lateinit var sut: UnlockRandomCardIfEnoughCoinsUseCase

    override fun setup() {
        super.setup()
        val dataStore = MockDataStore()
        unlockedCardPairsLocalDataSource =
            UnlockedCardPairsLocalDataSource(dataStore, MockAllCardPairsDataSource)
        cardRepository = CardRepository(MockAllCardPairsDataSource, unlockedCardPairsLocalDataSource)
        userRepository = UserRepository(MockEncryptor(), UserLocalDataSource(dataStore))
        val getUnlockedCardPairsCountUseCase =
            GetUnlockedCardPairsCountUseCase(testDispatcher, cardRepository)
        calculateNextCardPairCostUseCase = CalculateNextCardPairCostUseCase(
            testDispatcher,
            getUnlockedCardPairsCountUseCase,
            GetLevelsUseCase(testDispatcher, getUnlockedCardPairsCountUseCase),
            cardRepository
        )
        sut = UnlockRandomCardIfEnoughCoinsUseCase(
            testDispatcher,
            calculateNextCardPairCostUseCase,
            cardRepository,
            userRepository
        )
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
    fun `when user has more than enough coins then should remove correct amount of coins`() = runTest {
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
