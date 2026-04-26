@file:OptIn(org.jetbrains.compose.resources.ExperimentalResourceApi::class)

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
import com.wojdor.memolki.util.formatter.EpochDayFormatter
import org.jetbrains.compose.resources.setResourceReaderAndroidContext
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import sergio.sastre.composable.preview.scanner.android.AndroidComposablePreviewScanner
import sergio.sastre.composable.preview.scanner.android.AndroidPreviewInfo
import sergio.sastre.composable.preview.scanner.core.preview.ComposablePreview
import sergio.sastre.composable.preview.scanner.jvm.JvmAnnotationScanner

@RunWith(TestParameterInjector::class)
class PreviewTest(
    @TestParameter(valuesProvider = PreviewProvider::class)
    private val preview: ComposablePreview<*>
) {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5.copy(nightMode = NightMode.NOTNIGHT),
        renderingMode = RenderingMode.SHRINK,
        maxPercentDifference = 0.001
    )

    @Before
    fun configureComposeResources() {
        setResourceReaderAndroidContext(paparazzi.context)
        startKoin {
            modules(
                module {
                    single<EpochDayFormatter> { com.wojdor.memolki.util.formatter.AndroidEpochDayFormatter() }
                }
            )
        }
    }

    @After
    fun stopKoinAfterTest() {
        stopKoin()
    }

    @Test
    fun snap() {
        paparazzi.snapshot {
            val showBackground = (preview.previewInfo as? AndroidPreviewInfo)?.showBackground == true
            if (showBackground) {
                Box(modifier = Modifier.background(Color.White)) {
                    preview()
                }
            } else {
                preview()
            }
        }
    }

    class PreviewProvider : TestParameter.TestParameterValuesProvider {
        override fun provideValues(): List<ComposablePreview<*>> {
            val androidPreviews = AndroidComposablePreviewScanner()
                .scanPackageTrees("com.wojdor.memolki.ui")
                .includePrivatePreviews()
                .getPreviews()
            val cmpPreviews = JvmAnnotationScanner("org.jetbrains.compose.ui.tooling.preview.Preview")
                .scanPackageTrees("com.wojdor.memolki.ui")
                .includePrivatePreviews()
                .getPreviews()
            return androidPreviews + cmpPreviews
        }
    }
}
