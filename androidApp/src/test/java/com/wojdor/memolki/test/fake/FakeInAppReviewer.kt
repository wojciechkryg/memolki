package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.review.InAppReviewer

class FakeInAppReviewer : InAppReviewer {
    var requestCount: Int = 0
        private set

    override suspend fun request() {
        requestCount++
    }
}
