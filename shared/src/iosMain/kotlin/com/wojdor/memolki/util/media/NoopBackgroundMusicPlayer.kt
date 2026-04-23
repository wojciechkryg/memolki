package com.wojdor.memolki.util.media

class NoopBackgroundMusicPlayer : BackgroundMusicPlayer {
    override fun start() = Unit
    override fun pause() = Unit
}
