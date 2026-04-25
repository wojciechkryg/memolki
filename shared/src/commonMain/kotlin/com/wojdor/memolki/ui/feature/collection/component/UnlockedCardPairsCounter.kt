package com.wojdor.memolki.ui.feature.collection.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.wojdor.memolki.ui.feature.collection.CollectionState
import com.wojdor.memolki.ui.feature.collection.getCollectionStateForPreview
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun UnlockedCardPairsCounter(
    modifier: Modifier = Modifier,
    state: CollectionState
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = "${state.unlockedCardPairsCount} / ${state.allCardPairsCount}",
        style = MaterialTheme.typography.displaySmall,
        textAlign = TextAlign.Center
    )
}

@Preview
@Composable
fun UnlockedCardPairsCounterPreview() {
    AppTheme {
        UnlockedCardPairsCounter(state = getCollectionStateForPreview())
    }
}
