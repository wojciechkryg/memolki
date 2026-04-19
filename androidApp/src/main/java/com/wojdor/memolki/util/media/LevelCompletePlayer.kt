package com.wojdor.memolki.util.media

import android.content.Context
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.usecase.ObserveSoundEnabledUseCase
import kotlinx.coroutines.CoroutineDispatcher

class LevelCompletePlayer(
    context: Context,
    coroutineDispatcher: CoroutineDispatcher,
    observeSoundEnabledUseCase: ObserveSoundEnabledUseCase
) : SoundPlayer(context, coroutineDispatcher, observeSoundEnabledUseCase) {

    override val soundId: Int = R.raw.sound_level_complete
}
