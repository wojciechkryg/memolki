package com.wojdor.memolki.ui.feature.menu.component

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
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.shared.resources.*
import com.wojdor.memolki.ui.component.AutoSizeText
import com.wojdor.memolki.ui.component.EdgeSparklesEffect
import com.wojdor.memolki.ui.component.bounceClickEffect
import com.wojdor.memolki.ui.component.pulseEffect
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.animated
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.wojdor.memolki.ui.theme.spacingL
import com.wojdor.memolki.ui.theme.spacingM
import com.wojdor.memolki.util.throttleClick

@Composable
fun DailyRewardItem(onClick: () -> Unit = {}) {
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
                painter = painterResource(Res.drawable.ic_daily_reward),
                contentDescription = stringResource(Res.string.daily_reward)
            )
            Spacer(modifier = Modifier.size(spacingM))
            AutoSizeText(
                text = stringResource(Res.string.daily_reward).uppercase(),
                style = MaterialTheme.typography.headlineSmall.animated()
            )
        }
    }
}

@Preview
@Composable
private fun DailyRewardItemPreview() {
    AppTheme {
        DailyRewardItem()
    }
}
