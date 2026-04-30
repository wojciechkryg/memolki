package com.wojdor.memolki.util.media

import com.wojdor.memolki.domain.model.SettingModel
import com.wojdor.memolki.domain.usecase.GetSettingsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle

class IosHapticFeedback(
    coroutineDispatcher: CoroutineDispatcher,
    private val getSettingsUseCase: GetSettingsUseCase,
) : HapticFeedback {

    private val scope = CoroutineScope(coroutineDispatcher + SupervisorJob())
    private var isVibrationEnabled = false
    private val lightGenerator = UIImpactFeedbackGenerator(style = UIImpactFeedbackStyle.UIImpactFeedbackStyleLight)
    private val heavyGenerator = UIImpactFeedbackGenerator(style = UIImpactFeedbackStyle.UIImpactFeedbackStyleHeavy)

    init {
        scope.launch {
            getSettingsUseCase().collect { result ->
                result.onSuccess { settings ->
                    isVibrationEnabled = settings.filterIsInstance<SettingModel.Vibration>()
                        .first().isEnabled
                }
            }
        }
    }

    override fun vibrateLow() {
        if (!isVibrationEnabled) return
        lightGenerator.prepare()
        lightGenerator.impactOccurred()
    }

    override fun vibrateStrong() {
        if (!isVibrationEnabled) return
        heavyGenerator.prepare()
        heavyGenerator.impactOccurred()
    }
}
