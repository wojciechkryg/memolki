package com.wojdor.memolki.ui.feature.game.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.components.XmlDrawable
import com.wojdor.memolki.ui.theme.CardShape
import com.wojdor.memolki.util.rememberThrottleClick

@Composable
fun BackCardItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    CardBorder(
        modifier = modifier
            .clip(CardShape)
            .clickable(
                onClick = rememberThrottleClick(onClick = onClick),
            )
    ) {
        XmlDrawable(
            modifier = Modifier.fillMaxSize(),
            drawableRes = R.drawable.bg_card_back
        )
    }
}

@Preview
@Composable
private fun BackCardItemPreview() {
    BackCardItem(Modifier.size(128.dp))
}
