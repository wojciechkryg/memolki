package com.wojdor.memolki.util.extension

import platform.UIKit.UIActivityViewController

class IosTextSharer : TextSharer {

    override fun share(text: String) {
        val top = topViewController() ?: return
        val activityVc = UIActivityViewController(
            activityItems = listOf(text),
            applicationActivities = null,
        )
        top.presentViewController(activityVc, animated = true, completion = null)
    }
}
