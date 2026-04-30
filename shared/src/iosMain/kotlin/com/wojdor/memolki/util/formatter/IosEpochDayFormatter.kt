package com.wojdor.memolki.util.formatter

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterMediumStyle
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.dateWithTimeIntervalSince1970

class IosEpochDayFormatter : EpochDayFormatter {

    private val formatter = NSDateFormatter().apply {
        dateStyle = NSDateFormatterMediumStyle
        locale = NSLocale.currentLocale
    }

    override fun format(epochDay: Long): String {
        val date = NSDate.dateWithTimeIntervalSince1970(epochDay * SECONDS_PER_DAY.toDouble())
        return formatter.stringFromDate(date)
    }

    private companion object {
        const val SECONDS_PER_DAY = 86_400L
    }
}
