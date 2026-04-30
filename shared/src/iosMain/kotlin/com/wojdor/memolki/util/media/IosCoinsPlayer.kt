package com.wojdor.memolki.util.media

import com.wojdor.memolki.domain.usecase.ObserveSoundEnabledUseCase
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineDispatcher

@OptIn(ExperimentalForeignApi::class)
class IosCoinsPlayer(
    coroutineDispatcher: CoroutineDispatcher,
    observeSoundEnabledUseCase: ObserveSoundEnabledUseCase,
) : IosSoundPlayer(coroutineDispatcher, observeSoundEnabledUseCase), CoinsPlayer {
    override val soundName: String = SoundAssets.SOUND_COINS
}
