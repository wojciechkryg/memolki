package com.wojdor.memolki.domain.model

import com.wojdor.memolki.domain.model.StarCalculator.Companion.MAX_STARS
import com.wojdor.memolki.domain.model.StarCalculator.Companion.MIN_STARS
import com.wojdor.memolki.domain.model.StarCalculator.Companion.TWO_STARS
import org.junit.Assert.assertEquals
import org.junit.Test

class StarCalculatorTest {

    private val sut = StarCalculator()

    @Test
    fun `when zero mistakes then return max stars`() {
        // when
        val result = sut.calculate(0)

        // then
        assertEquals(MAX_STARS, result)
    }

    @Test
    fun `when one mistake then return two stars`() {
        // when
        val result = sut.calculate(1)

        // then
        assertEquals(TWO_STARS, result)
    }

    @Test
    fun `when four mistakes then return two stars`() {
        // when
        val result = sut.calculate(4)

        // then
        assertEquals(TWO_STARS, result)
    }

    @Test
    fun `when five mistakes then return min stars`() {
        // when
        val result = sut.calculate(5)

        // then
        assertEquals(MIN_STARS, result)
    }

    @Test
    fun `when many mistakes then return min stars`() {
        // when
        val result = sut.calculate(100)

        // then
        assertEquals(MIN_STARS, result)
    }
}
