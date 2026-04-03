package com.wojdor.memolki.ui.feature.endgame.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.component.AutoSizeText
import com.wojdor.memolki.ui.component.EdgeSparklesEffect
import com.wojdor.memolki.ui.component.bounceClickEffect
import com.wojdor.memolki.ui.component.pulseEffect
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.animated
import com.wojdor.memolki.ui.theme.spacingL
import com.wojdor.memolki.ui.theme.spacingM
import com.wojdor.memolki.util.throttleClick

@Composable
fun FreeCoinsItem(onClick: () -> Unit = {}) {
    EdgeSparklesEffect(
        modifier = Modifier
            .pulseEffect()
            .bounceClickEffect()
    ) {
        Button(
            onClick = throttleClick(onClick = onClick),
            shape = RoundedCornerShape(spacingL),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
        ) {
            Image(
                modifier = Modifier.size(64.dp),
                alignment = Alignment.Center,
                painter = painterResource(R.drawable.ic_daily_reward),
                contentDescription = stringResource(R.string.free_coins)
            )
            Spacer(modifier = Modifier.size(spacingM))
            AutoSizeText(
                text = stringResource(R.string.free_coins).uppercase(),
                style = MaterialTheme.typography.headlineSmall.animated()
            )
        }
    }
}

@Preview
@Composable
private fun FreeCoinsItemPreview() {
    AppTheme {
        FreeCoinsItem()
    }
}
