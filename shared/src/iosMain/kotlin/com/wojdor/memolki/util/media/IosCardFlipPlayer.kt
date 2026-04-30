package com.wojdor.memolki.util.media

import com.wojdor.memolki.domain.usecase.ObserveSoundEnabledUseCase
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.random.Random

@OptIn(ExperimentalForeignApi::class)
class IosCardFlipPlayer(
    coroutineDispatcher: CoroutineDispatcher,
    observeSoundEnabledUseCase: ObserveSoundEnabledUseCase,
) : IosSoundPlayer(coroutineDispatcher, observeSoundEnabledUseCase), CardFlipPlayer {

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
