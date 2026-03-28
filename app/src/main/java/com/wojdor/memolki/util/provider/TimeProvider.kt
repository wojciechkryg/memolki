package com.wojdor.memolki.util.provider

import java.time.LocalDate
import javax.inject.Inject

open class TimeProvider @Inject constructor() {

    open fun currentTimeMillis(): Long = System.currentTimeMillis()

    open fun currentLocalDate(): LocalDate = LocalDate.now()
}
