package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.media.BackgroundMusicPlayer

class FakeBackgroundMusicPlayer : BackgroundMusicPlayer {
    var startCount: Int = 0
        private set
    var pauseCount: Int = 0
        private set

    override fun start() {
        startCount++
    }

    override fun pause() {
        pauseCount++
    }
}
