package com.wojdor.memolki.util

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.core.net.toUri
import com.wojdor.memolki.util.provider.ActivityProvider

class AndroidAppOpener(
    private val activityProvider: ActivityProvider
) : AppOpener {

    override fun showAppInstall(appId: String) {
        val activity = activityProvider.current ?: return
        try {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "market://details?id=$appId".toUri()
                )
            )
        } catch (error: ActivityNotFoundException) {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=$appId".toUri()
                )
            )
        }
    }

    override fun openApp(appId: String) {
        val activity = activityProvider.current ?: return
        activity.packageManager.getLaunchIntentForPackage(appId)?.let {
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            activity.startActivity(it)
        }
    }
}
