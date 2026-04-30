package com.wojdor.memolki.util.media

import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryAmbient

private var configured = false

@OptIn(ExperimentalForeignApi::class)
internal fun configureAudioSession() {
    if (configured) return
    AVAudioSession.sharedInstance().setCategory(AVAudioSessionCategoryAmbient, error = null)
    configured = true
}
