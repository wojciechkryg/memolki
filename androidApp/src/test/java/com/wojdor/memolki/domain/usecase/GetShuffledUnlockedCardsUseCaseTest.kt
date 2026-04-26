package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.local.datastore.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.random.Random
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class GetShuffledUnlockedCardsUseCaseTest : AppTest() {

    private val cardRepository: CardRepository by inject()

    private val unlockedCardPairsLocalDataSource: UnlockedCardPairsLocalDataSource by inject()

    private lateinit var sut: GetShuffledUnlockedCardsUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when there are unlocked cards then return shuffled list`() = runTest {
        // given
        unlockedCardPairsLocalDataSource.addUnlockedCardPairId("watermelon")
        unlockedCardPairsLocalDataSource.addUnlockedCardPairId("mango")

        // when
        val result = sut(BoardModel.Grid2x3()).first()

        // then
        assertEquals(6, result.getOrThrow().size)
    }
}
