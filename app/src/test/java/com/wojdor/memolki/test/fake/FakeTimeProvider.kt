package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.provider.TimeProvider
import java.time.LocalDate
import javax.inject.Inject

class FakeTimeProvider @Inject constructor() : TimeProvider() {

    var mockCurrentDate: LocalDate = LocalDate.of(2026, 3, 26)

    override fun currentTimeMillis(): Long = mockCurrentDate.toEpochDay()

    override fun currentLocalDate(): LocalDate = mockCurrentDate
}
