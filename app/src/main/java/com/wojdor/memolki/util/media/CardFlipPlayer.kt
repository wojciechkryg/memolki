package com.wojdor.memolki.util.media

import android.content.Context
import android.media.MediaPlayer
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.SettingModel
import com.wojdor.memolki.domain.usecase.GetSettingsUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class CardFlipPlayer @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val getSettingsUseCase: GetSettingsUseCase
) {
    private val sounds = listOf(
        R.raw.sound_card_flip_1,
        R.raw.sound_card_flip_2,
        R.raw.sound_card_flip_3,
        R.raw.sound_card_flip_4
    )

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var isSoundEnabled = false

    fun play() {
        scope.launch {
            getSettingsUseCase().collect { result ->
                result.onSuccess { settings ->
                    isSoundEnabled = settings.find { it is SettingModel.Sound }?.isEnabled ?: false
                }
            }
            if (isSoundEnabled) {
                val soundId = sounds[Random.nextInt(sounds.size)]
                MediaPlayer.create(context, soundId).apply {
                    setVolume(VOLUME, VOLUME)
                    setOnCompletionListener { it.release() }
                    start()
                }
            }
        }
    }

    companion object {
        private const val VOLUME = 0.5f
    }
}
