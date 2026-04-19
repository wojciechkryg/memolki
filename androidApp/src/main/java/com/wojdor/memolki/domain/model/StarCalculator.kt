package com.wojdor.memolki.domain.model

import javax.inject.Inject

// TODO(kmp): move to commonMain after Phase 6 Koin swap drops the @Inject annotation.
class StarCalculator @Inject constructor() {

    fun calculate(mistakeCount: Int): Int = when {
        mistakeCount == 0 -> MAX_STARS
        mistakeCount in 1..4 -> TWO_STARS
        else -> MIN_STARS
    }

    companion object {
        const val MAX_STARS = 3
        const val TWO_STARS = 2
        const val MIN_STARS = 1
    }
}
