package com.wojdor.memolki.util.provider

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject

open class TimeProvider @Inject constructor() {

    open fun currentTimeMillis(): Long = System.currentTimeMillis()

    open fun currentLocalDate(): LocalDate = LocalDate.now()

    fun toLocalDate(timestampMillis: Long): LocalDate =
        Instant.ofEpochMilli(timestampMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

    fun daysBetween(from: LocalDate, to: LocalDate): Long =
        ChronoUnit.DAYS.between(from, to)
}
