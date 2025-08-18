package com.wojdor.memolki.ui.feature.game

import com.wojdor.memolki.AppTest
import com.wojdor.memolki.domain.model.LevelModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GameViewModelTest : AppTest() {

    private lateinit var sut: GameViewModel

    @Before
    override fun setup() {
        super.setup()
        sut = GameViewModel(
            savedStateHandle = savedStateHandle,
        )
    }

    @Test
    fun `when setLevel is called then state is updated with new level`() {
        // given
        val level = LevelModel.Grid2x3

        // when
        sut.setLevel(level)

        // then
        assertEquals(level, sut.uiState.value.level)
    }
}
