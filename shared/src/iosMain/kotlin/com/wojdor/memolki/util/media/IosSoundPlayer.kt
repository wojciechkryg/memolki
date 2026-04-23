package com.wojdor.memolki.util.media

open class IosSoundPlayer : SoundPlayer {
    override fun play() = Unit
    override suspend fun playDelayed() = Unit
}

class IosCardFlipPlayer : IosSoundPlayer(), CardFlipPlayer
class IosCardPairMatchedPlayer : IosSoundPlayer(), CardPairMatchedPlayer
class IosCoinsPlayer : IosSoundPlayer(), CoinsPlayer
class IosLevelCompletePlayer : IosSoundPlayer(), LevelCompletePlayer
