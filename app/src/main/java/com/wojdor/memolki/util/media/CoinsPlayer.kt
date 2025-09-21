package com.wojdor.memolki.util.media

import android.content.Context
import com.wojdor.memolki.R
import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.usecase.GetSettingsUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoinsPlayer @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    getSettingsUseCase: GetSettingsUseCase
) : SoundPlayer(context, coroutineDispatcher, getSettingsUseCase) {

    override val soundId: Int = R.raw.sound_coins
}
