package com.wojdor.memolki.util.media

class NoopHapticFeedback : HapticFeedback {
    override fun vibrateLow() = Unit
    override fun vibrateStrong() = Unit
}
