package com.wojdor.memolki.util.media

import android.content.Context
import com.wojdor.memolki.R
import com.wojdor.memolki.di.coroutine.MainDispatcher
import com.wojdor.memolki.domain.usecase.ObserveSoundEnabledUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoinsPlayer @Inject constructor(
    @ApplicationContext context: Context,
    @MainDispatcher coroutineDispatcher: CoroutineDispatcher,
    observeSoundEnabledUseCase: ObserveSoundEnabledUseCase
) : SoundPlayer(context, coroutineDispatcher, observeSoundEnabledUseCase) {

    override val soundId: Int = R.raw.sound_coins
}
