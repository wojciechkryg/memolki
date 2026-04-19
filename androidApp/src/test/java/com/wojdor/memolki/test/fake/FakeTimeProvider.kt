package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.provider.TimeProvider
import java.time.LocalDate
import java.time.ZoneOffset

class FakeTimeProvider : TimeProvider() {

    var mockCurrentDate: LocalDate = LocalDate.of(2026, 3, 26)

    override fun currentTimeMillis(): Long =
        mockCurrentDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

    override fun currentLocalDate(): LocalDate = mockCurrentDate
}
