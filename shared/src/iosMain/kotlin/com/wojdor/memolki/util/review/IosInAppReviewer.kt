package com.wojdor.memolki.util.review

// TODO(kmp-ios): replace with SKStoreReviewController.requestReview() when iOS review flow ships.
class IosInAppReviewer : InAppReviewer {
    override suspend fun request() = Unit
}
