package com.wojdor.memolki.util.provider

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner

open class AppForegroundProvider {

    open fun isAppInForeground(): Boolean =
        ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
}
