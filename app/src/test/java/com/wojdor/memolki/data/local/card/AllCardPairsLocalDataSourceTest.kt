package com.wojdor.memolki.data.local.card

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AllCardPairsLocalDataSourceTest {

    private val sut = AllCardPairsLocalDataSource()

    @Test
    fun `getAllCardPairs returns non-empty list`() {
        // when
        val result = sut.getAllCardPairs()

        // then
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun `getAllCardPairs returns unique ids`() {
        // when
        val result = sut.getAllCardPairs()

        // then
        val ids = result.map { it.id }
        assertEquals(ids.size, ids.distinct().size)
    }

    @Test
    fun `each card pair has two cards`() {
        // when
        val result = sut.getAllCardPairs()

        // then
        result.forEach { pair ->
            assertNotNull(pair.pair.first)
            assertNotNull(pair.pair.second)
        }
    }

    @Test
    fun `getCardPairById returns correct pair`() {
        // given
        val allPairs = sut.getAllCardPairs()
        val first = allPairs.first()

        // when
        val result = sut.getCardPairById(first.id)

        // then
        assertEquals(first, result)
    }

    @Test
    fun `getCardPairById returns null for unknown id`() {
        // when
        val result = sut.getCardPairById("nonexistent_card_pair_id")

        // then
        assertNull(result)
    }
}
