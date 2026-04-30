package com.wojdor.memolki.util.media

import com.wojdor.memolki.domain.usecase.ObserveMusicEnabledUseCase
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import platform.AVFAudio.AVAudioPlayer

@OptIn(ExperimentalForeignApi::class)
class IosBackgroundMusicPlayer(
    coroutineDispatcher: CoroutineDispatcher,
    private val observeMusicEnabledUseCase: ObserveMusicEnabledUseCase,
) : BackgroundMusicPlayer {

    private val scope = CoroutineScope(coroutineDispatcher + SupervisorJob())
    private var player: AVAudioPlayer? = null
    private var pendingPause: Job? = null
    private var isMusicEnabled = false

    init {
        configureAudioSession()
        scope.launch {
            observeMusicEnabledUseCase().collect { result ->
                result.onSuccess { enabled ->
                    isMusicEnabled = enabled
                    if (enabled) start() else pause()
                }
            }
        }
    }

    override fun start() {
        if (!isMusicEnabled) return
        pendingPause?.cancel()
        pendingPause = null
        val current = player ?: createPlayer(SoundAssets.MUSIC_BACKGROUND)?.also { it.numberOfLoops = -1 }
        player = current ?: return
        current.setVolume(NO_VOLUME, fadeDuration = 0.0)
        current.play()
        current.setVolume(MediaConstants.BACKGROUND_MUSIC_VOLUME, fadeDuration = FADE_DURATION_SECONDS)
    }

    override fun pause() {
        val current = player ?: return
        pendingPause?.cancel()
        if (!current.playing) {
            pendingPause = null
            return
        }
        current.setVolume(NO_VOLUME, fadeDuration = FADE_DURATION_SECONDS)
        pendingPause = scope.launch {
            delay(MediaConstants.MUSIC_FADE_DURATION_MS)
            current.pause()
        }
    }

    private companion object {
        const val NO_VOLUME = 0.0f
        const val FADE_DURATION_SECONDS = MediaConstants.MUSIC_FADE_DURATION_MS / 1000.0
    }
}
