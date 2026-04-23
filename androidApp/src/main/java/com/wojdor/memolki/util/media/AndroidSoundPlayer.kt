package com.wojdor.memolki.util.media

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.annotation.RawRes
import com.wojdor.memolki.domain.usecase.ObserveSoundEnabledUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class AndroidSoundPlayer(
    private val context: Context,
    coroutineDispatcher: CoroutineDispatcher,
    private val observeSoundEnabledUseCase: ObserveSoundEnabledUseCase
) : SoundPlayer {
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
            observeSoundEnabledUseCase().collect { result ->
                result.onSuccess { enabled ->
                    isSoundEnabled = enabled
                }
            }
        }
    }

    override suspend fun playDelayed() {
        delay(PLAY_DELAY)
        play()
    }

    override fun play() {
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
        private const val PLAY_DELAY = 300L
        private const val VOLUME = 0.5f
    }
}
