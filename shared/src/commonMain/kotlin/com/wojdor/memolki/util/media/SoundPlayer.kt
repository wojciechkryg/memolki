package com.wojdor.memolki.util.media

interface SoundPlayer {
    fun play()
    suspend fun playDelayed()
}

interface CardFlipPlayer : SoundPlayer
interface CardPairMatchedPlayer : SoundPlayer
interface CoinsPlayer : SoundPlayer
interface LevelCompletePlayer : SoundPlayer
