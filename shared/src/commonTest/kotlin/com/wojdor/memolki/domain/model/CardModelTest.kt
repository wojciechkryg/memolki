package com.wojdor.memolki.domain.model

import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.empty

import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlin.test.Test

class CardModelTest {

    @Test
    fun `Empty copyState returns same instance`() {
        // when
        val result = CardModel.Empty.copyState(isFlippedFront = true)

        // then
        assertSame(CardModel.Empty, result)
    }

    @Test
    fun `Empty has default values`() {
        // then
        assertFalse(CardModel.Empty.isFlippedFront)
        assertFalse(CardModel.Empty.isPairMatched)
        assertFalse(CardModel.Empty.isMatchAnimating)
        assertFalse(CardModel.Empty.isMistakeShaking)
    }

    @Test
    fun `Text copyState copies with new state`() {
        // given
        val card = CardModel.Text("id", "pairId", Res.string.empty)

        // when
        val result = card.copyState(isFlippedFront = true, isPairMatched = true)

        // then
        assertTrue(result.isFlippedFront)
        assertTrue(result.isPairMatched)
    }

    @Test
    fun `Image copyState copies with new state`() {
        // given
        val card = CardModel.Image("id", "pairId", Res.string.empty, 0)

        // when
        val result = card.copyState(isMatchAnimating = true, isMistakeShaking = true)

        // then
        assertTrue(result.isMatchAnimating)
        assertTrue(result.isMistakeShaking)
    }
}
