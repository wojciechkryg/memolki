package com.wojdor.memolki.util.formatter

import kotlinx.datetime.LocalDate

// TODO(kmp-ios): swap to NSDateFormatter localized MEDIUM style for native locale formatting.
class IosEpochDayFormatter : EpochDayFormatter {
    override fun format(epochDay: Long): String {
        val date = LocalDate.fromEpochDays(epochDay.toInt())
        return "${date.dayOfMonth}/${date.monthNumber}/${date.year}"
    }
}
