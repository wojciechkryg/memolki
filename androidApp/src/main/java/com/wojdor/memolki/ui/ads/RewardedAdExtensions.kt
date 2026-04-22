package com.wojdor.memolki.ui.ads

import android.app.Activity

fun RewardedAd.show(
    activity: Activity,
    onGrantReward: () -> Unit,
    onAdDismiss: (wasRewardGranted: Boolean) -> Unit = {}
) {
    (this as? AndroidRewardedAd)?.show(activity, onGrantReward, onAdDismiss)
}
