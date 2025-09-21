package com.wojdor.memolki.util.media

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.wojdor.memolki.R
import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.model.SettingModel
import com.wojdor.memolki.domain.usecase.GetSettingsUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardFlipPlayer @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val getSettingsUseCase: GetSettingsUseCase
) {
    private val sounds = listOf(
        R.raw.sound_card_flip_1,
        R.raw.sound_card_flip_2,
        R.raw.sound_card_flip_3,
        R.raw.sound_card_flip_4
    )

    private val scope = CoroutineScope(coroutineDispatcher + SupervisorJob())
    private var isSoundEnabled = false
    private var lastSoundId = 0

    init {
        observeSoundSettings()
    }

    private fun observeSoundSettings() {
        scope.launch {
            getSettingsUseCase().collect { result ->
                result.onSuccess { settings ->
                    isSoundEnabled =
                        settings.filterIsInstance<SettingModel.Sound>().first().isEnabled
                }
            }
        }
    }

    fun play() {
        if (isSoundEnabled) {
            val soundId = sounds.filter { it != lastSoundId }.random()
            lastSoundId = soundId
            MediaPlayer.create(context, soundId).apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                setVolume(VOLUME, VOLUME)
                setOnCompletionListener { it.release() }
                start()
            }
        }
    }

    companion object {
        private const val VOLUME = 0.1f
    }
}
