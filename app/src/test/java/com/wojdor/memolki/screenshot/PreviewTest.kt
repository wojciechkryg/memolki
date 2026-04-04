package com.wojdor.memolki.screenshot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams.RenderingMode
import com.android.resources.NightMode
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sergio.sastre.composable.preview.scanner.android.AndroidComposablePreviewScanner
import sergio.sastre.composable.preview.scanner.android.AndroidPreviewInfo
import sergio.sastre.composable.preview.scanner.core.preview.ComposablePreview

@RunWith(TestParameterInjector::class)
class PreviewTest(
    @TestParameter(valuesProvider = PreviewProvider::class)
    private val preview: ComposablePreview<AndroidPreviewInfo>
) {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5.copy(nightMode = NightMode.NOTNIGHT),
        renderingMode = RenderingMode.SHRINK,
        maxPercentDifference = 0.1
    )

    @Test
    fun snap() {
        paparazzi.snapshot {
            if (preview.previewInfo.showBackground) {
                Box(modifier = Modifier.background(Color.White)) {
                    preview()
                }
            } else {
                preview()
            }
        }
    }

    class PreviewProvider : TestParameter.TestParameterValuesProvider {
        override fun provideValues(): List<ComposablePreview<AndroidPreviewInfo>> =
            AndroidComposablePreviewScanner()
                .scanPackageTrees("com.wojdor.memolki.ui")
                .includePrivatePreviews()
                .getPreviews()
    }
}
