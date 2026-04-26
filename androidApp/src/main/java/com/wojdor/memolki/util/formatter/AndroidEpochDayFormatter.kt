package com.wojdor.memolki.util.formatter

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

class AndroidEpochDayFormatter : EpochDayFormatter {

    override fun format(epochDay: Long): String {
        val date = LocalDate.ofEpochDay(epochDay)
        val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            .withLocale(Locale.getDefault())
        return date.format(formatter)
    }
}
