package com.wojdor.memolki.util.review

import android.content.Context
import com.google.android.play.core.review.ReviewManagerFactory
import com.wojdor.memolki.util.extension.logE
import com.wojdor.memolki.util.provider.ActivityProvider
import kotlinx.coroutines.tasks.await

class AndroidInAppReviewer(
    context: Context,
    private val activityProvider: ActivityProvider
) : InAppReviewer {

    private val reviewManager = ReviewManagerFactory.create(context)

    override suspend fun request() {
        val activity = activityProvider.current ?: return
        try {
            val reviewInfo = reviewManager.requestReviewFlow().await()
            reviewManager.launchReviewFlow(activity, reviewInfo).await()
        } catch (exception: Exception) {
            logE("Failed to request in-app review", exception)
        }
    }
}
