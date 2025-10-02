package com.wojdor.memolki.util.media

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.wojdor.memolki.R
import com.wojdor.memolki.di.coroutine.MainDispatcher
import com.wojdor.memolki.domain.model.SettingModel
import com.wojdor.memolki.domain.usecase.GetSettingsUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackgroundMusicPlayer @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:MainDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val getSettingsUseCase: GetSettingsUseCase
) : DefaultLifecycleObserver {

    // Fix for the default looping mechanism of MediaPlayer that causes a small gap between loops
    // by using two MediaPlayers and switching between them we can achieve gapless looping
    private var currentPlayer: MediaPlayer? = null
    private var nextPlayer: MediaPlayer? = null

    private val scope = CoroutineScope(coroutineDispatcher + SupervisorJob())
    private var volumeJob: Job? = null
    private var isMusicEnabled = false
    private var currentVolume = NO_VOLUME
    private var isFadingOut = false

    init {
        observeMusicSettings()
    }

    private fun observeMusicSettings() {
        scope.launch {
            getSettingsUseCase().collect { result ->
                result.onSuccess { settings ->
                    isMusicEnabled =
                        settings.filterIsInstance<SettingModel.Music>().first().isEnabled
                    playIfMusicEnabled()
                }
            }
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        playIfMusicEnabled()
    }

    override fun onPause(owner: LifecycleOwner) {
        pause()
    }

    fun start() {
        if (!isMusicEnabled || (currentPlayer?.isPlaying == true && !isFadingOut)) return

        volumeJob?.cancel()
        isFadingOut = false

        if (currentPlayer == null) {
            currentPlayer = createPlayer()
        }
        currentPlayer?.let {
            if (!it.isPlaying) {
                setVolume(NO_VOLUME)
                it.start()
            }
            fadeVolume(BACKGROUND_MUSIC_VOLUME, FADE_DURATION_MS)
            it.setNextMediaPlayer(null)
            nextPlayer?.release()
            nextPlayer = createPlayer().apply {
                setVolume(BACKGROUND_MUSIC_VOLUME, BACKGROUND_MUSIC_VOLUME)
            }
            it.setNextMediaPlayer(nextPlayer)
            it.setOnCompletionListener(onCompletionListener)
        }
    }

    fun pause() {
        if (currentPlayer?.isPlaying == true) {
            volumeJob?.cancel()
            isFadingOut = true
            fadeVolume(NO_VOLUME, FADE_DURATION_MS) {
                if (isFadingOut) {
                    currentPlayer?.pause()
                    isFadingOut = false
                }
            }
        }
    }

    private fun playIfMusicEnabled() {
        if (isMusicEnabled) {
            start()
        } else {
            stop()
        }
    }

    private fun stop() {
        if (currentPlayer?.isPlaying == true) {
            volumeJob?.cancel()
            isFadingOut = true
            fadeVolume(NO_VOLUME, FADE_DURATION_MS) {
                if (isFadingOut) {
                    releasePlayers()
                }
            }
        } else {
            releasePlayers()
        }
    }

    private fun releasePlayers() {
        volumeJob?.cancel()
        isFadingOut = false
        currentVolume = NO_VOLUME
        currentPlayer?.apply {
            setOnCompletionListener(null)
            setNextMediaPlayer(null)
            stop()
            release()
        }
        currentPlayer = null
        nextPlayer?.apply {
            setOnCompletionListener(null)
            release()
        }
        nextPlayer = null
    }

    private fun createPlayer() = MediaPlayer.create(context, R.raw.music_background).apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
    }

    private fun fadeVolume(to: Float, duration: Long, onEnd: () -> Unit = {}) {
        volumeJob?.cancel()
        val from = currentVolume
        volumeJob = scope.launch {
            val steps = (duration / FADE_STEP_DURATION).toInt()
            for (i in 0..steps) {
                val progress = i.toFloat() / steps
                val volume = from + (to - from) * progress
                setVolume(volume)
                delay(FADE_STEP_DURATION)
            }
            setVolume(to)
            onEnd()
        }
    }

    private fun setVolume(volume: Float) {
        currentPlayer?.setVolume(volume, volume)
        currentVolume = volume
    }

    private val onCompletionListener: MediaPlayer.OnCompletionListener =
        MediaPlayer.OnCompletionListener {
            currentPlayer = nextPlayer
            nextPlayer = createPlayer().apply {
                setVolume(BACKGROUND_MUSIC_VOLUME, BACKGROUND_MUSIC_VOLUME)
            }
            currentPlayer?.setNextMediaPlayer(nextPlayer)
            currentPlayer?.setOnCompletionListener(onCompletionListener)
            it.release()
        }

    companion object {
        private const val BACKGROUND_MUSIC_VOLUME = 0.25f
        private const val NO_VOLUME = 0.0f
        private const val FADE_DURATION_MS = 500L
        private const val FADE_STEP_DURATION = 50L
    }
}
