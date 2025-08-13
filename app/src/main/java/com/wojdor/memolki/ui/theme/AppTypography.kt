package com.wojdor.memolki.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.wojdor.memolki.R

private val PatrickHandFont = FontFamily(
    Font(R.font.patrickhand_regular, FontWeight.Normal)
)

val AppTypography = Typography().run {
    copy(
        displayLarge = displayLarge.copy(fontFamily = PatrickHandFont),
        displayMedium = displayMedium.copy(fontFamily = PatrickHandFont),
        displaySmall = displaySmall.copy(fontFamily = PatrickHandFont),
        headlineLarge = headlineLarge.copy(fontFamily = PatrickHandFont),
        headlineMedium = headlineMedium.copy(fontFamily = PatrickHandFont),
        headlineSmall = headlineSmall.copy(fontFamily = PatrickHandFont),
        titleLarge = titleLarge.copy(fontFamily = PatrickHandFont),
        titleMedium = titleMedium.copy(fontFamily = PatrickHandFont),
        titleSmall = titleSmall.copy(fontFamily = PatrickHandFont),
        bodyLarge = bodyLarge.copy(fontFamily = PatrickHandFont),
        bodyMedium = bodyMedium.copy(fontFamily = PatrickHandFont),
        bodySmall = bodySmall.copy(fontFamily = PatrickHandFont),
        labelLarge = labelLarge.copy(fontFamily = PatrickHandFont),
        labelMedium = labelMedium.copy(fontFamily = PatrickHandFont),
        labelSmall = labelSmall.copy(fontFamily = PatrickHandFont)
    )
}
