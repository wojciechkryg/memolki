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
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.ui.component.PreviewBackground
import com.wojdor.memolki.ui.theme.AppColors
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.FullRoundedShape
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun GameProgressBar(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = 100f
        ),
        label = "progress bar animation"
    )
    Box(
        modifier = modifier
            .height(PROGRESS_BAR_HEIGHT)
            .fillMaxWidth()
            .clip(FullRoundedShape)
            .border(CARD_BORDER_SIZE, AppColors.Border, FullRoundedShape)
    ) {
        if (animatedProgress > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress.coerceIn(MIN_VISIBLE_FRACTION, 1f))
                    .clip(FullRoundedShape)
                    .background(AppColors.Font)
            )
        }
    }
}

private val PROGRESS_BAR_HEIGHT = 10.dp
private const val MIN_VISIBLE_FRACTION = 0.05f

@Preview
@Composable
private fun GameProgressBarEmptyPreview() {
    AppTheme {
        PreviewBackground {
            GameProgressBar(progress = 0f)
        }
    }
}

@Preview
@Composable
private fun GameProgressBarHalfPreview() {
    AppTheme {
        PreviewBackground {
            GameProgressBar(progress = 0.5f)
        }
    }
}

@Preview
@Composable
private fun GameProgressBarFullPreview() {
    AppTheme {
        PreviewBackground {
            GameProgressBar(progress = 1f)
        }
    }
}
