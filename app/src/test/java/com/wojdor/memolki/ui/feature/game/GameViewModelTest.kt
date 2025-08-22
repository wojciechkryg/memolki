package com.wojdor.memolki.ui.feature.game

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.data.source.card.local.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.GetShuffledUnlockedCards
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockAllCardPairsDataSource
import com.wojdor.memolki.test.mock.MockSharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GameViewModelTest : AppTest() {

    private lateinit var sut: GameViewModel

    @Before
    override fun setup() {
        super.setup()
        sut = GameViewModel(
            savedStateHandle,
            GetShuffledUnlockedCards(
                testDispatcher,
                CardRepository(
                    MockAllCardPairsDataSource, UnlockedCardPairsLocalDataSource(
                        MockSharedPreferences(), MockAllCardPairsDataSource
                    )
                )
            )
        )
    }

    @Test
    fun `when OnLevelStart intent is sent then state is updated with new level`() = runTest {
        sut.uiState.test {
            // given
            val level = LevelModel.Grid2x3

            // when
            sut.sendIntent(GameIntent.OnLevelStart(level))

            // then
            assertEquals(LevelModel.Empty, awaitItem().level)
            assertEquals(LevelModel.Grid2x3, awaitItem().level)
        }
    }

    @Test
    fun `when OnCardClick intent is sent then state is updated with flipped card`() = runTest {
        sut.uiState.test {
            // given
            sut.sendIntent(GameIntent.OnLevelStart(LevelModel.Grid2x3))
            skipItems(1)
            val cardToClick = awaitItem().cards[0][0]

            // when
            sut.sendIntent(GameIntent.OnBackCardClick(cardToClick))

            // then
            assertEquals(true, awaitItem().cards[0][0].isFlipped)
        }
    }
}
