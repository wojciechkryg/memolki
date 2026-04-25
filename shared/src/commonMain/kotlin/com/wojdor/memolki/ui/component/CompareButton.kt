package com.wojdor.memolki.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.daily_challenge_compare
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.FullRoundedShape
import com.wojdor.memolki.ui.theme.animated
import com.wojdor.memolki.ui.theme.spacingL
import com.wojdor.memolki.ui.theme.spacingS
import com.wojdor.memolki.ui.theme.spacingXL
import com.wojdor.memolki.util.throttleClick
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CompareButton(
    onClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = spacingXL + spacingL,
        vertical = spacingL
    ),
    textStyle: TextStyle = MaterialTheme.typography.displaySmall
) {
    EdgeSparklesEffect(
        modifier = Modifier
            .pulseEffect()
            .bounceClickEffect()
    ) {
        Button(
            onClick = throttleClick(onClick = onClick),
            contentPadding = contentPadding,
            shape = FullRoundedShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            modifier = Modifier
                .clip(FullRoundedShape)
                .shimmerEffect()
        ) {
            Text(
                text = stringResource(Res.string.daily_challenge_compare).uppercase(),
                style = textStyle.animated()
            )
        }
    }
}

@Preview
@Composable
private fun CompareButtonPreview() {
    AppTheme {
        PreviewBackground {
            CompareButton(onClick = {})
        }
    }
}

@Preview
@Composable
private fun CompareButtonSmallPreview() {
    AppTheme {
        PreviewBackground {
            CompareButton(
                onClick = {},
                contentPadding = PaddingValues(horizontal = spacingL, vertical = spacingS),
                textStyle = MaterialTheme.typography.headlineLarge
            )
        }
    }
}
