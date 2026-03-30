package com.wojdor.memolki.ui.feature.endgame.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.component.AutoSizeText
import com.wojdor.memolki.ui.component.bounceClickEffect
import com.wojdor.memolki.ui.feature.collection.component.CARD_PAIR_VERTICAL_PADDING
import com.wojdor.memolki.ui.feature.collection.component.CollectionLockedCard
import com.wojdor.memolki.ui.feature.collection.component.cardLeftModifier
import com.wojdor.memolki.ui.feature.collection.component.cardRightModifier
import com.wojdor.memolki.ui.feature.collection.component.rotatedCardPairShape
import com.wojdor.memolki.ui.feature.game.component.CARD_BORDER_SIZE
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.CardShape
import com.wojdor.memolki.ui.theme.spacingXS
import com.wojdor.memolki.util.throttleClick

@Composable
fun UnlockNewCardItem(
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .width(144.dp)
            .bounceClickEffect()
            .clip(rotatedCardPairShape)
            .clickable(
                onClick = throttleClick(onClick = onClick),
            )
            .padding(vertical = CARD_PAIR_VERTICAL_PADDING),
        contentAlignment = Alignment.Center
    ) {
        CollectionLockedCard(modifier = cardLeftModifier)
        Box(
            modifier = cardRightModifier,
            contentAlignment = Alignment.Center
        ) {
            CollectionLockedCard()
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(CARD_BORDER_SIZE)
                    .background(color = Color.White.copy(alpha = 0.5f), CardShape)
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                AutoSizeText(
                    modifier = Modifier.padding(start = spacingXS),
                    text = stringResource(R.string.new_card_to_unlock).uppercase(),
                    style = MaterialTheme.typography.displaySmall,
                )
            }
        }
    }

}

@Preview
@Composable
private fun UnlockNewCardItemPreview() {
    AppTheme {
        UnlockNewCardItem()
    }
}
