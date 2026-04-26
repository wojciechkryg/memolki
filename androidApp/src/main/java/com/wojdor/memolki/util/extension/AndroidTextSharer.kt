package com.wojdor.memolki.util.extension

import android.content.Intent
import com.wojdor.memolki.util.provider.ActivityProvider

class AndroidTextSharer(
    private val activityProvider: ActivityProvider
) : TextSharer {

    override fun share(text: String) {
        val activity = activityProvider.current ?: return
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        activity.startActivity(Intent.createChooser(intent, null))
    }
}
