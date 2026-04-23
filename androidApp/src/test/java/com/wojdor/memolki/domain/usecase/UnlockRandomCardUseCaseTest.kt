package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.local.datastore.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.fake.FakeAllCardPairsDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.random.Random
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class UnlockRandomCardUseCaseTest : AppTest() {

    private val unlockedCardPairsLocalDataSource: UnlockedCardPairsLocalDataSource by inject()

    private val cardRepository: CardRepository by inject()

    private val random: Random by inject()

    private lateinit var sut: UnlockRandomCardUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = get()
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
