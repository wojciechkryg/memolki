package com.wojdor.memolki.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.wojdor.memolki.R

@Composable
fun getAppTypography() = Typography().run {
    copy(
        displayLarge = displayLarge.withAppFont().withAppColor(),
        displayMedium = displayMedium.withAppFont().withAppColor(),
        displaySmall = displaySmall.withAppFont().withAppColor(),
        headlineLarge = headlineLarge.withAppFont().withAppColor(),
        headlineMedium = headlineMedium.withAppFont().withAppColor(),
        headlineSmall = headlineSmall.withAppFont().withAppColor(),
        titleLarge = titleLarge.withAppFont().withAppColor(),
        titleMedium = titleMedium.withAppFont().withAppColor(),
        titleSmall = titleSmall.withAppFont().withAppColor(),
        bodyLarge = bodyLarge.withAppFont().withAppColor(),
        bodyMedium = bodyMedium.withAppFont().withAppColor(),
        bodySmall = bodySmall.withAppFont().withAppColor(),
        labelLarge = labelLarge.withAppFont().withAppColor(),
        labelMedium = labelMedium.withAppFont().withAppColor(),
        labelSmall = labelSmall.withAppFont().withAppColor()
    )
}

private fun TextStyle.withAppFont(): TextStyle {
    return this.copy(fontFamily = PatrickHandFont)
}

@Composable
private fun TextStyle.withAppColor(): TextStyle {
    return this.copy(color = colorResource(R.color.font))
}

private val PatrickHandFont = FontFamily(
    Font(R.font.patrickhand_regular, FontWeight.Normal)
)
