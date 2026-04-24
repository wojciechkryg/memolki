package com.wojdor.memolki.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.patrickhand_regular
import org.jetbrains.compose.resources.Font

@Composable
fun appTypography() = MaterialTheme.typography.run {
    val appFont = FontFamily(Font(Res.font.patrickhand_regular, FontWeight.Normal))
    copy(
        displayLarge = displayLarge.withFont(appFont).withAppColor(),
        displayMedium = displayMedium.withFont(appFont).withAppColor(),
        displaySmall = displaySmall.withFont(appFont).withAppColor(),
        headlineLarge = headlineLarge.withFont(appFont).withAppColor(),
        headlineMedium = headlineMedium.withFont(appFont).withAppColor(),
        headlineSmall = headlineSmall.withFont(appFont).withAppColor(),
        titleLarge = titleLarge.withFont(appFont).withAppColor(),
        titleMedium = titleMedium.withFont(appFont).withAppColor(),
        titleSmall = titleSmall.withFont(appFont).withAppColor(),
        bodyLarge = bodyLarge.withFont(appFont).withAppColor(),
        bodyMedium = bodyMedium.withFont(appFont).withAppColor(),
        bodySmall = bodySmall.withFont(appFont).withAppColor(),
        labelLarge = labelLarge.withFont(appFont).withAppColor(),
        labelMedium = labelMedium.withFont(appFont).withAppColor(),
        labelSmall = labelSmall.withFont(appFont).withAppColor()
    )
}

private fun TextStyle.withFont(family: FontFamily): TextStyle = copy(fontFamily = family)

private fun TextStyle.withAppColor(): TextStyle = copy(color = AppColors.Font)
