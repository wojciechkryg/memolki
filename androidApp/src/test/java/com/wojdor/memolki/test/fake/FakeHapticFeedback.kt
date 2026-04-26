package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.media.HapticFeedback

class FakeHapticFeedback : HapticFeedback {
    var vibrateLowCount: Int = 0
        private set
    var vibrateStrongCount: Int = 0
        private set

    override fun vibrateLow() {
        vibrateLowCount++
    }

    override fun vibrateStrong() {
        vibrateStrongCount++
    }
}
