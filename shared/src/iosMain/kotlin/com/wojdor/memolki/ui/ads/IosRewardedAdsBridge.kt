package com.wojdor.memolki.ui.ads

interface IosRewardedAdsBridge {
    fun load(adUnitId: String, completion: (Boolean) -> Unit)
    fun isLoaded(adUnitId: String): Boolean
    fun show(adUnitId: String, onReward: () -> Unit, onDismiss: () -> Unit)
}
