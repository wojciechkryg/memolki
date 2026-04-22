package com.wojdor.memolki.ui.component

import com.wojdor.memolki.ui.theme.AppColors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R

@Composable
fun FadeEffectTop(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(FADE_EFFECT_HEIGHT)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        AppColors.Primary,
                        Color.Transparent
                    )
                )
            )
    )
}

internal val FADE_EFFECT_HEIGHT = 6.dp

