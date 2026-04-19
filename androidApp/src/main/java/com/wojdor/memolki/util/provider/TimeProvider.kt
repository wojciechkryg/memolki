package com.wojdor.memolki.util.provider

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

open class TimeProvider {

    open fun currentTimeMillis(): Long = System.currentTimeMillis()

    open fun currentLocalDate(): LocalDate = LocalDate.now()

    fun toLocalDate(timestampMillis: Long): LocalDate =
        Instant.ofEpochMilli(timestampMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

    fun localDateFromEpochDay(epochDay: Long): LocalDate =
        LocalDate.ofEpochDay(epochDay)

    fun daysBetween(from: LocalDate, to: LocalDate): Long =
        ChronoUnit.DAYS.between(from, to)
}
