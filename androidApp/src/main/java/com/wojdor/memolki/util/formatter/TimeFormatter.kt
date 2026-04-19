package com.wojdor.memolki.util.formatter

class TimeFormatter {

    fun format(timeMillis: Long): FormattedTime {
        val safeTimeMillis = timeMillis.coerceAtLeast(0)
        val totalSeconds = safeTimeMillis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val millis = safeTimeMillis % 1000
        return FormattedTime(
            main = "$minutes:${seconds.toString().padStart(2, '0')}",
            millis = ".${millis.toString().padStart(3, '0')}"
        )
    }

    data class FormattedTime(val main: String, val millis: String) {
        override fun toString(): String = "$main$millis"
    }
}
