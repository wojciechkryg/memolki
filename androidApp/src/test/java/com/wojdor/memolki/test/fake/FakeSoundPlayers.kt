package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.media.CardFlipPlayer
import com.wojdor.memolki.util.media.CardPairMatchedPlayer
import com.wojdor.memolki.util.media.CoinsPlayer
import com.wojdor.memolki.util.media.LevelCompletePlayer
import com.wojdor.memolki.util.media.SoundPlayer

open class FakeSoundPlayer : SoundPlayer {
    var playCount: Int = 0
        private set
    var playDelayedCount: Int = 0
        private set

    override fun play() {
        playCount++
    }

    override suspend fun playDelayed() {
        playDelayedCount++
    }
}

class FakeCardFlipPlayer : FakeSoundPlayer(), CardFlipPlayer
class FakeCardPairMatchedPlayer : FakeSoundPlayer(), CardPairMatchedPlayer
class FakeCoinsPlayer : FakeSoundPlayer(), CoinsPlayer
class FakeLevelCompletePlayer : FakeSoundPlayer(), LevelCompletePlayer
