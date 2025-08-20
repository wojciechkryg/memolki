package com.wojdor.memolki.ui.feature.game.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.components.DrawableImage
import com.wojdor.memolki.ui.theme.CardShape

@Composable
fun BackCardItem(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .border(
                border = BorderStroke(2.dp, colorResource(R.color.border)),
                shape = CardShape
            )
            .clip(CardShape)
    ) {
        DrawableImage(
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
