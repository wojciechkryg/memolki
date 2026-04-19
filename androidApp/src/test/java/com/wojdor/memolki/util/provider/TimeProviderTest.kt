package com.wojdor.memolki.util.provider

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class TimeProviderTest {

    private val sut = TimeProvider()

    @Test
    fun `toLocalDate converts timestamp to local date`() {
        // given
        val expected = LocalDate.of(2024, 1, 1)
        val timestamp = expected
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        // when
        val result = sut.toLocalDate(timestamp)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `localDateFromEpochDay converts epoch day to local date`() {
        // given
        val expected = LocalDate.of(2026, 4, 15)
        val epochDay = expected.toEpochDay()

        // when
        val result = sut.localDateFromEpochDay(epochDay)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `daysBetween returns correct number of days`() {
        // given
        val from = LocalDate.of(2024, 1, 1)
        val to = LocalDate.of(2024, 1, 10)

        // when
        val result = sut.daysBetween(from, to)

        // then
        assertEquals(9, result)
    }

    @Test
    fun `daysBetween returns zero for same day`() {
        // given
        val date = LocalDate.of(2024, 6, 15)

        // when
        val result = sut.daysBetween(date, date)

        // then
        assertEquals(0, result)
    }

    @Test
    fun `daysBetween returns negative for reversed dates`() {
        // given
        val from = LocalDate.of(2024, 1, 10)
        val to = LocalDate.of(2024, 1, 1)

        // when
        val result = sut.daysBetween(from, to)

        // then
        assertEquals(-9, result)
    }
}
