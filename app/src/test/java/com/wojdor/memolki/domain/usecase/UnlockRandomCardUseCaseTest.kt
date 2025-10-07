package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.local.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockAllCardPairsDataSource
import com.wojdor.memolki.test.mock.MockDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class UnlockRandomCardUseCaseTest : AppTest() {

    private lateinit var unlockedCardPairsLocalDataSource: UnlockedCardPairsLocalDataSource
    private lateinit var cardRepository: CardRepository
    private lateinit var sut: UnlockRandomCardUseCase

    override fun setup() {
        super.setup()
        val dataStore = MockDataStore()
        unlockedCardPairsLocalDataSource =
            UnlockedCardPairsLocalDataSource(dataStore, MockAllCardPairsDataSource)
        cardRepository =
            CardRepository(MockAllCardPairsDataSource, unlockedCardPairsLocalDataSource)
        sut = UnlockRandomCardUseCase(testDispatcher, cardRepository)
    }

    @Test
    fun `when there are no locked cards then do nothing`() = runTest {
        // given
        MockAllCardPairsDataSource.getAllCardPairs().forEach {
            unlockedCardPairsLocalDataSource.addUnlockedCardPairId(it.id)
        }

        // when
        sut()

        // then
        assertEquals(
            MockAllCardPairsDataSource.getAllCardPairs().size,
            unlockedCardPairsLocalDataSource.getUnlockedCardPairIds().size
        )
    }
}
