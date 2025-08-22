package com.wojdor.memolki.ui.feature.game.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.ui.components.AutoSizeText
import com.wojdor.memolki.ui.theme.AppTypography

@Composable
fun FrontCardItem(
    modifier: Modifier = Modifier,
    card: CardModel
) {
    CardBorder(modifier = modifier) {
        when (card) {
            is CardModel.Text -> FrontCardItemText(card)
            is CardModel.Image -> FrontCardItemImage(card)
            CardModel.Empty -> Unit
        }
    }
}

@Composable
private fun FrontCardItemText(card: CardModel.Text) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        contentAlignment = Alignment.Center
    ) {
        AutoSizeText(
            text = stringResource(card.textRes),
            style = AppTypography.displayLarge,
        )
    }
}

@Composable
private fun FrontCardItemImage(card: CardModel.Image) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(card.imageRes),
            contentDescription = stringResource(card.textRes)
        )
    }
}

@Preview
@Composable
private fun FrontCardItemTextPreview() {
    FrontCardItem(
        Modifier.size(128.dp),
        CardModel.Text(
            pairId = "banana",
            textRes = R.string.banana
        )
    )
}

@Preview
@Composable
private fun FrontCardItemImagePreview() {
    FrontCardItem(
        Modifier.size(128.dp),
        CardModel.Image(
            pairId = "banana",
            textRes = R.string.banana,
            imageRes = R.drawable.img_banana_whole
        )
    )
}
