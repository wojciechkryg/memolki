package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.util.billing.BillingHandler
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class UnlockAllNewCardPairsIfPurchasedUseCaseTest : AppTest() {

    private val billingHandler: BillingHandler by inject()

    private val cardRepository: CardRepository by inject()

    private val unlockAllCardPairsUseCase: UnlockAllCardPairsUseCase by inject()

    private lateinit var sut: UnlockAllNewCardPairsIfPurchasedUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when there are locked cards and unlock all is purchased then unlock all card pairs`() =
        runTest {
            // given
            coEvery { billingHandler.isPurchased(BillingHandler.IAP_UNLOCK_ALL_CARDS) } returns true

            // when
            val result = sut().first()

            // then
            assertTrue(result.isSuccess)
            assertTrue(cardRepository.getLockedCardPairs().isEmpty())
        }

    @Test
    fun `when there are locked cards and unlock all is not purchased then do not unlock all card pairs`() =
        runTest {
            // given
            coEvery { billingHandler.isPurchased(BillingHandler.IAP_UNLOCK_ALL_CARDS) } returns false

            // when
            val result = sut().first()

            // then
            assertTrue(result.isSuccess)
            assertTrue(cardRepository.getLockedCardPairs().isNotEmpty())
        }
}
