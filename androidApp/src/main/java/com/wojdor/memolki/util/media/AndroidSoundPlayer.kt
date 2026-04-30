package com.wojdor.memolki.util.media

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
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
    abstract val soundName: String

    private val scope = CoroutineScope(coroutineDispatcher + SupervisorJob())
    private val playerCache = mutableMapOf<String, MediaPlayer>()
    protected var isSoundEnabled = false
        private set

    init {
        observeSoundSettings()
    }

    private fun observeSoundSettings() {
        scope.launch {
            observeSoundEnabledUseCase().collect { result ->
                result.onSuccess { enabled -> isSoundEnabled = enabled }
            }
        }
    }

    override suspend fun playDelayed() {
        delay(MediaConstants.SOUND_PLAY_DELAY_MS)
        play()
    }

    override fun play() {
        if (!isSoundEnabled) return
        val name = soundName
        val player = playerCache.getOrPut(name) { createPlayer(name) }
        player.seekTo(0)
        player.start()
    }

    private fun createPlayer(name: String): MediaPlayer {
        val afd = context.openAudioAsset(name)
        return MediaPlayer().apply {
            setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            afd.close()
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            prepare()
            setVolume(MediaConstants.SOUND_VOLUME, MediaConstants.SOUND_VOLUME)
        }
    }
}
