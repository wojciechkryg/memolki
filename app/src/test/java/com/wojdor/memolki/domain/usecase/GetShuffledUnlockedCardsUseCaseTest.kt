package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.local.datastore.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject
import kotlin.random.Random

@ExperimentalCoroutinesApi
class GetShuffledUnlockedCardsUseCaseTest : AppTest() {

    @Inject
    lateinit var cardRepository: CardRepository

    @Inject
    lateinit var unlockedCardPairsLocalDataSource: UnlockedCardPairsLocalDataSource

    private lateinit var sut: GetShuffledUnlockedCardsUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetShuffledUnlockedCardsUseCase(
            testDispatcher,
            cardRepository,
            Random(0)
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when there are unlocked cards then return shuffled list`() = runTest {
        // given
        unlockedCardPairsLocalDataSource.addUnlockedCardPairId("watermelon")
        unlockedCardPairsLocalDataSource.addUnlockedCardPairId("mango")

        // when
        val result = sut(LevelModel.Grid2x3()).first()

        // then
        assertEquals(6, result.getOrThrow().size)
    }
}
