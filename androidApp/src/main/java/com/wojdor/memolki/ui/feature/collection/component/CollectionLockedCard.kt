package com.wojdor.memolki.ui.feature.collection.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.component.XmlDrawable
import com.wojdor.memolki.ui.feature.game.component.CardBorder
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun CollectionLockedCard(modifier: Modifier = Modifier) {
    CardBorder(modifier = modifier) {
        XmlDrawable(
            modifier = Modifier.fillMaxSize(),
            drawableRes = R.drawable.bg_card_back
        )
    }
}

@Preview
@Composable
fun LockedCardPreview() {
    AppTheme {
        CollectionLockedCard(Modifier.size(128.dp))
    }
}
