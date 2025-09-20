package com.wojdor.memolki.util.media

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.wojdor.memolki.di.coroutine.DefaultDispatcher
import com.wojdor.memolki.domain.model.SettingModel
import com.wojdor.memolki.domain.usecase.GetSettingsUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HapticFeedback @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getSettingsUseCase: GetSettingsUseCase,
    @DefaultDispatcher private val coroutineDispatcher: CoroutineDispatcher
) {
    private val scope = CoroutineScope(coroutineDispatcher + SupervisorJob())
    private var isVibrationEnabled: Boolean = false

    private val vibrator by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    init {
        observeVibrationSettings()
    }

    private fun observeVibrationSettings() {
        scope.launch {
            getSettingsUseCase().collect { result ->
                result.onSuccess { settings ->
                    isVibrationEnabled =
                        settings.filterIsInstance<SettingModel.Vibration>().first().isEnabled
                }
            }
        }
    }

    fun vibrateLow() {
        if (!isVibrationEnabled) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    VIBRATE_LOW_MS,
                    VIBRATE_LOW_AMPLITUDE
                )
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(VIBRATE_LOW_MS)
        }
    }

    fun vibrateStrong() {
        if (!isVibrationEnabled) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    VIBRATE_STRONG_MS,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(VIBRATE_STRONG_MS)
        }
    }

    companion object {
        private const val VIBRATE_LOW_MS = 50L
        private const val VIBRATE_LOW_AMPLITUDE = 50
        private const val VIBRATE_STRONG_MS = 150L
    }
}
