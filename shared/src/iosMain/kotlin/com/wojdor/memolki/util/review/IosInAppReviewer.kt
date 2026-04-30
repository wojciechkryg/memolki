package com.wojdor.memolki.util.review

import platform.StoreKit.SKStoreReviewController
import platform.UIKit.UIApplication
import platform.UIKit.UIWindowScene

class IosInAppReviewer : InAppReviewer {

    override suspend fun request() {
        val scene = UIApplication.sharedApplication.connectedScenes
            .firstOrNull { it is UIWindowScene } as? UIWindowScene
            ?: return
        SKStoreReviewController.requestReviewInScene(scene)
    }
}
