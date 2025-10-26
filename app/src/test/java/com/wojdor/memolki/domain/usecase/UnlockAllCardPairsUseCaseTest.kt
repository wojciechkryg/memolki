package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class UnlockAllCardPairsUseCaseTest : AppTest() {

    @Inject
    lateinit var cardRepository: CardRepository

    private lateinit var sut: UnlockAllCardPairsUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = UnlockAllCardPairsUseCase(testDispatcher, cardRepository)
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when use case is executed then unlock all card pairs in repository`() = runTest {
        // when
        sut().first()

        // then
        assertTrue(cardRepository.getLockedCardPairs().isEmpty())
    }
}
