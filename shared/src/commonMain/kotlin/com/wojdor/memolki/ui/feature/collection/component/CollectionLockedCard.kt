package com.wojdor.memolki.ui.feature.collection.component

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.ui.component.CardBackBackground
import com.wojdor.memolki.ui.feature.game.component.CardBorder
import com.wojdor.memolki.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CollectionLockedCard(modifier: Modifier = Modifier) {
    CardBorder(modifier = modifier) {
        CardBackBackground()
    }
}

@Preview
@Composable
private fun LockedCardPreview() {
    AppTheme {
        CollectionLockedCard(Modifier.size(128.dp))
    }
}
