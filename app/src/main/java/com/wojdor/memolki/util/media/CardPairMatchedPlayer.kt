package com.wojdor.memolki.util.media

import android.content.Context
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.usecase.GetSettingsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardPairMatchedPlayer @Inject constructor(
    context: Context,
    coroutineDispatcher: CoroutineDispatcher,
    getSettingsUseCase: GetSettingsUseCase
) : SoundPlayer(context, coroutineDispatcher, getSettingsUseCase) {

    override val soundId: Int = R.raw.sound_card_pair_matched
}
