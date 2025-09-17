package com.wojdor.memolki.util

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.usecase.GetMusicEnabledUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackgroundMusicPlayer @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val getMusicEnabledUseCase: GetMusicEnabledUseCase
) : DefaultLifecycleObserver {

    // Fix for the default looping mechanism of MediaPlayer that causes a small gap between loops
    // by using two MediaPlayers and switching between them we can achieve gapless looping
    private var currentPlayer: MediaPlayer? = null
    private var nextPlayer: MediaPlayer? = null

    override fun onStart(owner: LifecycleOwner) {
        getMusicEnabledUseCase()
            .onEach {
                if (it.getOrDefault(false)) {
                    start()
                } else {
                    stop()
                }
            }
            .filter { it.isSuccess }
            .launchIn(owner.lifecycle.coroutineScope)
    }

    override fun onStop(owner: LifecycleOwner) {
        pause()
    }

    fun start() {
        if (currentPlayer == null) {
            currentPlayer = createPlayer()
        }
        currentPlayer?.let {
            if (it.isPlaying) return
            it.start()
            nextPlayer = createPlayer()
            it.setNextMediaPlayer(nextPlayer)
            it.setOnCompletionListener(onCompletionListener)
        }
    }

    fun pause() {
        currentPlayer?.pause()
    }

    private fun stop() {
        currentPlayer?.stop()
        currentPlayer?.release()
        currentPlayer = null
        nextPlayer?.release()
        nextPlayer = null
    }

    private fun createPlayer() = MediaPlayer.create(context, R.raw.music_background).apply {
        setVolume(BACKGROUND_MUSIC_VOLUME, BACKGROUND_MUSIC_VOLUME)
    }

    private val onCompletionListener: MediaPlayer.OnCompletionListener =
        MediaPlayer.OnCompletionListener {
            it.release()
            currentPlayer = nextPlayer
            nextPlayer = createPlayer()
            currentPlayer?.setNextMediaPlayer(nextPlayer)
            currentPlayer?.setOnCompletionListener(onCompletionListener)
        }

    companion object {
        private const val BACKGROUND_MUSIC_VOLUME = 0.3f
    }
}
