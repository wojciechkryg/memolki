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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.FullRoundedShape
import com.wojdor.memolki.ui.theme.animated
import com.wojdor.memolki.ui.theme.spacingL
import com.wojdor.memolki.ui.theme.spacingS
import com.wojdor.memolki.ui.theme.spacingXL
import com.wojdor.memolki.util.throttleClick

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
                text = stringResource(R.string.daily_challenge_compare).uppercase(),
                style = textStyle.animated()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CompareButtonPreview() {
    AppTheme {
        CompareButton(onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun CompareButtonSmallPreview() {
    AppTheme {
        CompareButton(
            onClick = {},
            contentPadding = PaddingValues(horizontal = spacingL, vertical = spacingS),
            textStyle = MaterialTheme.typography.headlineLarge
        )
    }
}
