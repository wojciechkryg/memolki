package com.wojdor.memolki.util.provider

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner

actual open class AppForegroundProvider actual constructor() {

    actual open fun isAppInForeground(): Boolean =
        ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
}
