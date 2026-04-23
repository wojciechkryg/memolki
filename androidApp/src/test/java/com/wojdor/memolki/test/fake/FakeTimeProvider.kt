package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.provider.TimeProvider
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

class FakeTimeProvider : TimeProvider() {

    var mockCurrentDate: LocalDate = LocalDate(2026, 3, 26)

    override fun currentTimeMillis(): Long =
        mockCurrentDate.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()

    override fun currentLocalDate(): LocalDate = mockCurrentDate
}
