package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.util.billing.BillingHandler
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class UnlockAllNewCardPairsIfPurchasedUseCaseTest : AppTest() {

    @Inject
    lateinit var billingHandler: BillingHandler

    @Inject
    lateinit var cardRepository: CardRepository

    @Inject
    lateinit var unlockAllCardPairsUseCase: UnlockAllCardPairsUseCase

    private lateinit var sut: UnlockAllNewCardPairsIfPurchasedUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = UnlockAllNewCardPairsIfPurchasedUseCase(
            testDispatcher,
            billingHandler,
            cardRepository,
            unlockAllCardPairsUseCase
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when there are locked cards and unlock all is purchased then unlock all card pairs`() =
        runTest {
            // given
            coEvery { billingHandler.isPurchased(any()) } returns true

            // when
            sut().first()

            // then
            assertTrue(cardRepository.getLockedCardPairs().isEmpty())
        }

    @Test
    fun `when there are locked cards and unlock all is not purchased then unlock all card pairs`() =
        runTest {
            // given
            coEvery { billingHandler.isPurchased(any()) } returns false

            // when
            sut().first()

            // then
            assertTrue(cardRepository.getLockedCardPairs().isNotEmpty())
        }
}
