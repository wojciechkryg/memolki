package com.wojdor.memolki.util.provider

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
open class TimeProvider {

    open fun currentTimeMillis(): Long = Clock.System.now().toEpochMilliseconds()

    open fun currentLocalDate(): LocalDate =
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    fun toLocalDate(timestampMillis: Long): LocalDate =
        Instant.fromEpochMilliseconds(timestampMillis)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date

    fun localDateFromEpochDay(epochDay: Long): LocalDate =
        LocalDate.fromEpochDays(epochDay)

    fun daysBetween(from: LocalDate, to: LocalDate): Long =
        from.daysUntil(to).toLong()
}
