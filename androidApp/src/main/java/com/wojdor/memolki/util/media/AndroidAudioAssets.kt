package com.wojdor.memolki.util.media

import android.content.Context
import android.content.res.AssetFileDescriptor
import com.wojdor.memolki.shared.resources.Res

private const val ANDROID_ASSET_PREFIX = "file:///android_asset/"

internal fun Context.openAudioAsset(soundName: String): AssetFileDescriptor {
    val uri = Res.getUri("files/audio/$soundName.m4a")
    require(uri.startsWith(ANDROID_ASSET_PREFIX)) { "Unexpected URI scheme: $uri" }
    return assets.openFd(uri.removePrefix(ANDROID_ASSET_PREFIX))
}
