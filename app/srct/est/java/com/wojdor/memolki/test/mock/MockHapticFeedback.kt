package com.wojdor.memolki.test.mock

import com.wojdor.memolki.util.media.HapticFeedback
import javax.inject.Inject

class MockHapticFeedback @Inject constructor() : HapticFeedback {
    override fun mediumClick() {}
}
