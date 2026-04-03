package com.wojdor.memolki.ui.feature.collection.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.component.AutoSizeText
import com.wojdor.memolki.ui.component.EdgeSparklesEffect
import com.wojdor.memolki.ui.component.bounceClickEffect
import com.wojdor.memolki.ui.feature.game.component.CARD_BORDER_SIZE
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.CardShape
import com.wojdor.memolki.ui.theme.isLargeScreen
import com.wojdor.memolki.ui.theme.spacingL
import com.wojdor.memolki.util.throttleClick

@Composable
fun CollectionUnlockCardPairWithAd(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(if (isLargeScreen) spacingL else 0.dp)
            .bounceClickEffect()
            .clip(rotatedCardPairShape)
            .clickable(
                onClickLabel = stringResource(R.string.accessibility_unlock_with_ad),
                onClick = throttleClick(onClick = onClick)
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
            EdgeSparklesEffect(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(CARD_BORDER_SIZE)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White.copy(alpha = 0.5F), CardShape)
                        .padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = Modifier.size(64.dp),
                        painter = painterResource(R.drawable.ic_ads),
                        contentDescription = stringResource(R.string.watch_ad),
                    )
                    AutoSizeText(
                        text = stringResource(R.string.watch_ad).lowercase(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CollectionUnlockCardPairWithAdPreview() {
    AppTheme {
        CollectionUnlockCardPairWithAd(
            modifier = Modifier.width(192.dp),
        )
    }
}

