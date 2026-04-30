package com.wojdor.memolki.util.media

import com.wojdor.memolki.domain.usecase.ObserveSoundEnabledUseCase
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.wojdor.memolki.shared.resources.Res
import platform.AVFAudio.AVAudioPlayer
import platform.Foundation.NSURL

@OptIn(ExperimentalForeignApi::class)
abstract class IosSoundPlayer(
    coroutineDispatcher: CoroutineDispatcher,
    private val observeSoundEnabledUseCase: ObserveSoundEnabledUseCase,
) : SoundPlayer {

    protected abstract val soundName: String

    private val scope = CoroutineScope(coroutineDispatcher + SupervisorJob())
    private val playerCache = mutableMapOf<String, AVAudioPlayer>()
    private var isSoundEnabled = false

    init {
        configureAudioSession()
        scope.launch {
            observeSoundEnabledUseCase().collect { result ->
                result.onSuccess { isSoundEnabled = it }
            }
        }
    }

    override fun play() {
        if (!isSoundEnabled) return
        val name = soundName
        val player = playerCache.getOrPut(name) { createPlayer(name) ?: return }
        player.currentTime = 0.0
        player.volume = MediaConstants.SOUND_VOLUME
        player.play()
    }

    override suspend fun playDelayed() {
        delay(MediaConstants.SOUND_PLAY_DELAY_MS)
        play()
    }
}

@OptIn(ExperimentalForeignApi::class)
internal fun createPlayer(name: String): AVAudioPlayer? {
    val uri = Res.getUri("files/audio/$name.m4a")
    val url = NSURL.URLWithString(uri) ?: NSURL.fileURLWithPath(uri)
    return AVAudioPlayer(contentsOfURL = url, error = null)?.also { it.prepareToPlay() }
}
