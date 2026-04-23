package com.wojdor.memolki.util.media

open class NoopSoundPlayer : SoundPlayer {
    override fun play() = Unit
    override suspend fun playDelayed() = Unit
}

class NoopCardFlipPlayer : NoopSoundPlayer(), CardFlipPlayer
class NoopCardPairMatchedPlayer : NoopSoundPlayer(), CardPairMatchedPlayer
class NoopCoinsPlayer : NoopSoundPlayer(), CoinsPlayer
class NoopLevelCompletePlayer : NoopSoundPlayer(), LevelCompletePlayer
