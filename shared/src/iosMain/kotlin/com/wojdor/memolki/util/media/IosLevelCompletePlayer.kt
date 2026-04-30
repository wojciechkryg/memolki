package com.wojdor.memolki.util.media

import com.wojdor.memolki.domain.usecase.ObserveSoundEnabledUseCase
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineDispatcher

@OptIn(ExperimentalForeignApi::class)
class IosLevelCompletePlayer(
    coroutineDispatcher: CoroutineDispatcher,
    observeSoundEnabledUseCase: ObserveSoundEnabledUseCase,
) : IosSoundPlayer(coroutineDispatcher, observeSoundEnabledUseCase), LevelCompletePlayer {
    override val soundName: String = SoundAssets.SOUND_LEVEL_COMPLETE
}
