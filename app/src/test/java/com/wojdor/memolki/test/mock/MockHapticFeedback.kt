package com.wojdor.memolki.test.mock

import android.content.Context
import com.wojdor.memolki.domain.usecase.GetSettingsUseCase
import com.wojdor.memolki.util.media.HapticFeedback
import kotlinx.coroutines.CoroutineDispatcher

class MockHapticFeedback(
    context: Context,
    coroutineDispatcher: CoroutineDispatcher,
    getSettingsUseCase: GetSettingsUseCase
) : HapticFeedback(context, coroutineDispatcher, getSettingsUseCase) {

    override fun vibrateLow() {
        // Do nothing
    }

    override fun vibrateStrong() {
        // Do nothing
    }
}
