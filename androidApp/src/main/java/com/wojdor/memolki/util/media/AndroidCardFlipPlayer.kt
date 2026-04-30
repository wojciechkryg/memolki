package com.wojdor.memolki.util.media

import android.content.Context
import com.wojdor.memolki.domain.usecase.ObserveSoundEnabledUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.random.Random

class AndroidCardFlipPlayer(
    context: Context,
    coroutineDispatcher: CoroutineDispatcher,
    observeSoundEnabledUseCase: ObserveSoundEnabledUseCase
) : AndroidSoundPlayer(context, coroutineDispatcher, observeSoundEnabledUseCase), CardFlipPlayer {

    private var lastIndex: Int = -1

    override val soundName: String
        get() {
            val variants = SoundAssets.SOUND_CARD_FLIP_VARIANTS
            val index = if (lastIndex < 0) {
                Random.nextInt(variants.size)
            } else {
                var i = Random.nextInt(variants.size - 1)
                if (i >= lastIndex) i++
                i
            }
            lastIndex = index
            return variants[index]
        }
}
