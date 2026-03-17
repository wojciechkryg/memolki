package com.wojdor.memolki.ui.feature.settings.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.SettingModel
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.spacingL
import com.wojdor.memolki.ui.theme.spacingS
import com.wojdor.memolki.ui.theme.spacingXS

@Composable
fun ToggleSettingButton(
    setting: SettingModel,
    onClick: () -> Unit = {}
) {
    Button(
        // don't use throttleClick here
        onClick = onClick,
        contentPadding = PaddingValues(top = spacingS, bottom = spacingS, start = spacingS, end = spacingL),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black,
            disabledContainerColor = Color.Transparent
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = setting.resId),
                    tint = colorResource(R.color.font),
                    contentDescription = null,
                )
                AnimateDisabledStrikeThrough(setting)
            }
            Spacer(modifier = Modifier.size(spacingXS))
            Text(
                text = stringResource(id = setting.textId).uppercase(),
                style = MaterialTheme.typography.displaySmall
            )
        }
    }
}

@Composable
private fun AnimateDisabledStrikeThrough(setting: SettingModel) {
    val strikeThroughAmount by animateFloatAsState(
        targetValue = if (setting.isEnabled) 0f else 1f,
        animationSpec = tween(ANIMATION_DURATION)
    )
    if (strikeThroughAmount > 0f) {
        val color = colorResource(R.color.font)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 3.dp.toPx()

            val start = Offset(size.width / 4f, size.height / 4f)
            val end = Offset(size.width * 3 / 4f, size.height * 3 / 4f)

            val currentEnd = Offset(
                x = start.x + (end.x - start.x) * strikeThroughAmount,
                y = start.y + (end.y - start.y) * strikeThroughAmount
            )

            drawLine(
                color = color,
                start = start,
                end = currentEnd,
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }
    }
}

private const val ANIMATION_DURATION = 200

@Preview
@Composable
private fun ToggleSettingButtonEnabledPreview() {
    AppTheme {
        ToggleSettingButton(
            setting = SettingModel.Music(isEnabled = true)
        )
    }
}

@Preview
@Composable
private fun ToggleSettingButtonDisabledPreview() {
    AppTheme {
        ToggleSettingButton(
            setting = SettingModel.Music(isEnabled = false)
        )
    }
}

