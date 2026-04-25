package com.wojdor.memolki.ui.feature.game.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.accessibility_flip_card
import com.wojdor.memolki.ui.component.CardBackBackground
import com.wojdor.memolki.ui.component.bounceClickEffect
import com.wojdor.memolki.ui.theme.CardShape
import com.wojdor.memolki.util.throttleClick
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BackCardItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    CardBorder(
        modifier = modifier
            .bounceClickEffect()
            .clip(CardShape)
            .clickable(
                onClickLabel = stringResource(Res.string.accessibility_flip_card),
                onClick = throttleClick(onClick = onClick)
            )
    ) {
        CardBackBackground()
    }
}

@Preview
@Composable
private fun BackCardItemPreview() {
    BackCardItem(Modifier.size(128.dp))
}
