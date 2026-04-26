package com.wojdor.memolki.data.mapper

import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.assertEquals
import kotlin.test.Test

@ExperimentalCoroutinesApi
class CardFlipCountsMapperTest : AppTest() {

    @Test
    fun `when serialize grid then return semicolon and comma separated string`() {
        // given
        val grid = listOf(listOf(1, 2, 3), listOf(4, 5, 6))

        // when
        val result = CardFlipCountsMapper.serialize(grid)

        // then
        assertEquals("1,2,3;4,5,6", result)
    }

    @Test
    fun `when serialize empty grid then return empty string`() {
        // given
        val grid = emptyList<List<Int>>()

        // when
        val result = CardFlipCountsMapper.serialize(grid)

        // then
        assertEquals("", result)
    }

    @Test
    fun `when deserialize valid string then return grid`() {
        // given
        val value = "1,2,3;4,5,6"

        // when
        val result = CardFlipCountsMapper.deserialize(value)

        // then
        assertEquals(listOf(listOf(1, 2, 3), listOf(4, 5, 6)), result)
    }

    @Test
    fun `when deserialize empty string then return empty list`() {
        // given
        val value = ""

        // when
        val result = CardFlipCountsMapper.deserialize(value)

        // then
        assertEquals(emptyList<List<Int>>(), result)
    }

    @Test
    fun `when serialize and deserialize then return original grid`() {
        // given
        val grid = listOf(listOf(2, 1, 3), listOf(1, 2, 1), listOf(4, 2, 2))

        // when
        val result = CardFlipCountsMapper.deserialize(CardFlipCountsMapper.serialize(grid))

        // then
        assertEquals(grid, result)
    }
}
