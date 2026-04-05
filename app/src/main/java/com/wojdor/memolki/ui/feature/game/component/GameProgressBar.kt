package com.wojdor.memolki.ui.feature.game.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.component.EdgeSparklesEffect
import com.wojdor.memolki.ui.theme.FullRoundedShape

@Composable
fun GameProgressBar(
    progress: Float,
    isGameFinished: Boolean,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = 300f
        ),
        label = "progress bar animation"
    )
    val barContent: @Composable () -> Unit = {
        Box(
            modifier = modifier
                .height(PROGRESS_BAR_HEIGHT)
                .fillMaxWidth()
                .clip(FullRoundedShape)
                .border(CARD_BORDER_SIZE, colorResource(R.color.border), FullRoundedShape)
        ) {
            if (animatedProgress > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(animatedProgress.coerceIn(MIN_VISIBLE_FRACTION, 1f))
                        .clip(FullRoundedShape)
                        .background(colorResource(R.color.font))
                )
            }
        }
    }
    if (isGameFinished) {
        EdgeSparklesEffect { barContent() }
    } else {
        barContent()
    }
}

private val PROGRESS_BAR_HEIGHT = 10.dp
private const val MIN_VISIBLE_FRACTION = 0.05f
