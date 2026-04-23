package com.wojdor.memolki.util.media

import android.content.Context
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.usecase.ObserveSoundEnabledUseCase
import kotlinx.coroutines.CoroutineDispatcher

class AndroidLevelCompletePlayer(
    context: Context,
    coroutineDispatcher: CoroutineDispatcher,
    observeSoundEnabledUseCase: ObserveSoundEnabledUseCase
) : AndroidSoundPlayer(context, coroutineDispatcher, observeSoundEnabledUseCase), LevelCompletePlayer {

    override val soundId: Int = R.raw.sound_level_complete
}
