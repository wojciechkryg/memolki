package com.wojdor.memolki.util.media

import android.content.Context
import com.wojdor.memolki.domain.usecase.ObserveSoundEnabledUseCase
import kotlinx.coroutines.CoroutineDispatcher

class AndroidCardPairMatchedPlayer(
    context: Context,
    coroutineDispatcher: CoroutineDispatcher,
    observeSoundEnabledUseCase: ObserveSoundEnabledUseCase
) : AndroidSoundPlayer(context, coroutineDispatcher, observeSoundEnabledUseCase), CardPairMatchedPlayer {
    override val soundName: String = SoundAssets.SOUND_CARD_PAIR_MATCHED
}
