package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.local.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.test.fake.FakeAllCardPairsDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class UnlockRandomCardUseCaseTest : AppTest() {

    @Inject
    lateinit var unlockedCardPairsLocalDataSource: UnlockedCardPairsLocalDataSource

    @Inject
    lateinit var cardRepository: CardRepository

    private lateinit var sut: UnlockRandomCardUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = UnlockRandomCardUseCase(testDispatcher, cardRepository)
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when there are no locked cards then do nothing`() = runTest {
        // given
        FakeAllCardPairsDataSource().getAllCardPairs().forEach {
            unlockedCardPairsLocalDataSource.addUnlockedCardPairId(it.id)
        }

        // when
        sut().first()

        // then
        assertEquals(
            FakeAllCardPairsDataSource().getAllCardPairs().size,
            unlockedCardPairsLocalDataSource.getUnlockedCardPairIds().size
        )
    }
}
