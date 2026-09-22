package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.util.billing.BillingHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class GrantPurchaseRewardUseCaseTest : AppTest() {

    private val userRepository: UserRepository by inject()
    private val cardRepository: CardRepository by inject()

    private lateinit var sut: GrantPurchaseRewardUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when coins small is granted then coins are added`() = runTest {
        // when
        sut(GrantPurchaseRewardUseCase.Params(BillingHandler.IAP_COINS_SMALL, "purchase-1")).first()

        // then
        assertEquals(BillingHandler.COINS_SMALL_REWARD, userRepository.getCoins().first())
    }

    @Test
    fun `when coins big is granted then coins are added`() = runTest {
        // when
        sut(GrantPurchaseRewardUseCase.Params(BillingHandler.IAP_COINS_BIG, "purchase-2")).first()

        // then
        assertEquals(BillingHandler.COINS_BIG_REWARD, userRepository.getCoins().first())
    }

    @Test
    fun `when unlock all cards is granted then all card pairs are unlocked`() = runTest {
        // when
        sut(GrantPurchaseRewardUseCase.Params(BillingHandler.IAP_UNLOCK_ALL_CARDS, "purchase-3")).first()

        // then
        assertTrue(cardRepository.getLockedCardPairs().isEmpty())
    }

    @Test
    fun `when the same purchase id is delivered twice then the reward is granted once`() = runTest {
        // when
        sut(GrantPurchaseRewardUseCase.Params(BillingHandler.IAP_COINS_SMALL, "purchase-4")).first()
        sut(GrantPurchaseRewardUseCase.Params(BillingHandler.IAP_COINS_SMALL, "purchase-4")).first()

        // then
        assertEquals(BillingHandler.COINS_SMALL_REWARD, userRepository.getCoins().first())
    }

    @Test
    fun `when a new purchase id is delivered then the reward is granted again`() = runTest {
        // when
        sut(GrantPurchaseRewardUseCase.Params(BillingHandler.IAP_COINS_SMALL, "purchase-5")).first()
        sut(GrantPurchaseRewardUseCase.Params(BillingHandler.IAP_COINS_SMALL, "purchase-6")).first()

        // then
        assertEquals(BillingHandler.COINS_SMALL_REWARD * 2, userRepository.getCoins().first())
    }

    @Test
    fun `when product id is unknown then result is failure and nothing is granted`() = runTest {
        // when
        val result = sut(GrantPurchaseRewardUseCase.Params("unknown_product", "purchase-7")).first()

        // then
        assertTrue(result.isFailure)
        assertEquals(0L, userRepository.getCoins().first())
    }
}
