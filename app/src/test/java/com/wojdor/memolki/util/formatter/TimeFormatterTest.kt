package com.wojdor.memolki.util.formatter

import org.junit.Assert.assertEquals
import org.junit.Test

class TimeFormatterTest {

    private val sut = TimeFormatter()

    @Test
    fun `format zero millis`() {
        // when
        val result = sut.format(0L)

        // then
        assertEquals("0:00", result.main)
        assertEquals(".000", result.millis)
    }

    @Test
    fun `format one second`() {
        // when
        val result = sut.format(1000L)

        // then
        assertEquals("0:01", result.main)
        assertEquals(".000", result.millis)
    }

    @Test
    fun `format one minute`() {
        // when
        val result = sut.format(60_000L)

        // then
        assertEquals("1:00", result.main)
        assertEquals(".000", result.millis)
    }

    @Test
    fun `format complex time`() {
        // when
        val result = sut.format(125_456L)

        // then
        assertEquals("2:05", result.main)
        assertEquals(".456", result.millis)
    }

    @Test
    fun `format with millis padding`() {
        // when
        val result = sut.format(1_005L)

        // then
        assertEquals("0:01", result.main)
        assertEquals(".005", result.millis)
    }

    @Test
    fun `format negative time is coerced to zero`() {
        // when
        val result = sut.format(-1000L)

        // then
        assertEquals("0:00", result.main)
        assertEquals(".000", result.millis)
    }

    @Test
    fun `toString combines main and millis`() {
        // when
        val result = sut.format(125_456L)

        // then
        assertEquals("2:05.456", result.toString())
    }
}
