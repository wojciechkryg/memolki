package com.wojdor.memolki.util.media

import com.wojdor.memolki.domain.usecase.ObserveSoundEnabledUseCase
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineDispatcher

@OptIn(ExperimentalForeignApi::class)
class IosCardPairMatchedPlayer(
    coroutineDispatcher: CoroutineDispatcher,
    observeSoundEnabledUseCase: ObserveSoundEnabledUseCase,
) : IosSoundPlayer(coroutineDispatcher, observeSoundEnabledUseCase), CardPairMatchedPlayer {
    override val soundName: String = SoundAssets.SOUND_CARD_PAIR_MATCHED
}
