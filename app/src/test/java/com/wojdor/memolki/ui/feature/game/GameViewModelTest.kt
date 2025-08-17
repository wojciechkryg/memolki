package com.wojdor.memolki.ui.feature.game

import com.wojdor.memolki.AppTest
import com.wojdor.memolki.domain.model.LevelModel
import io.mockk.impl.annotations.InjectMockKs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GameViewModelTest : AppTest() {

    @InjectMockKs
    private lateinit var sut: GameViewModel

    @Test
    fun `when setLevel is called then state is updated with new level`() {
        // given
        val level = LevelModel.Level2x3

        // when
        sut.setLevel(level)

        // then
        assertEquals(level, sut.uiState.value.level)
    }
}
