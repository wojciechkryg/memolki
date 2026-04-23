package com.wojdor.memolki.util.media

import android.content.Context
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.usecase.ObserveSoundEnabledUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@OptIn(ExperimentalAtomicApi::class)
class AndroidCardFlipPlayer(
    context: Context,
    coroutineDispatcher: CoroutineDispatcher,
    observeSoundEnabledUseCase: ObserveSoundEnabledUseCase
) : AndroidSoundPlayer(context, coroutineDispatcher, observeSoundEnabledUseCase), CardFlipPlayer {

    private val sounds = listOf(
        R.raw.sound_card_flip_1,
        R.raw.sound_card_flip_2,
        R.raw.sound_card_flip_3,
        R.raw.sound_card_flip_4
    )

    private var lastSoundId = AtomicInt(0)

    override val soundId: Int
        get() {
            val last = lastSoundId.load()
            val nextSound = sounds.filter { it != last }.random()
            lastSoundId.store(nextSound)
            return nextSound
        }
}
