package com.wojdor.memolki.util.media

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.annotation.RawRes
import com.wojdor.memolki.domain.model.SettingModel
import com.wojdor.memolki.domain.usecase.GetSettingsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

abstract class SoundPlayer(
    private val context: Context,
    coroutineDispatcher: CoroutineDispatcher,
    private val getSettingsUseCase: GetSettingsUseCase
) {
    @get:RawRes
    abstract val soundId: Int
    private val scope = CoroutineScope(coroutineDispatcher + SupervisorJob())
    protected var isSoundEnabled = false
        private set

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
        private const val VOLUME = 0.5f
    }
}
