package com.wojdor.memolki.ui.feature.cardpairdetails.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.ui.component.AutoSizeText
import com.wojdor.memolki.ui.feature.game.component.FrontCardItem
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun CardPairDetailsContent(cardPairModel: CardPairModel) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        val cardSize = maxWidth.coerceAtMost(maxHeight)
        val firstCard = cardPairModel.first
        val isFirstCardText = firstCard is CardModel.Text
        val secondCard = cardPairModel.second
        val isSecondCardText = secondCard is CardModel.Text
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CardDetails(
                card = firstCard,
                cardSize = cardSize
            )
            if (!isFirstCardText && !isSecondCardText && firstCard.textRes != secondCard.textRes) {
                TextDetails(modifier = Modifier.weight(1f), textRes = firstCard.textRes)
            }
            CardDetails(
                card = secondCard,
                cardSize = cardSize
            )
            if (!isFirstCardText && !isSecondCardText) {
                TextDetails(modifier = Modifier.weight(1f), textRes = secondCard.textRes)
            }
        }
    }
}

@Composable
private fun CardDetails(card: CardModel, cardSize: Dp) {
    FrontCardItem(
        modifier = Modifier
            .size(cardSize)
            .padding(8.dp),
        card = card
    )
}

@Composable
private fun TextDetails(modifier: Modifier, @StringRes textRes: Int) {
    AutoSizeText(
        modifier = modifier
            .padding(8.dp),
        text = stringResource(textRes),
        style = MaterialTheme.typography.displaySmall,
    )
}

@Preview(showBackground = true)
@Composable
private fun CardPairDetailsContentImagesSameTextPreview() {
    AppTheme {
        CardPairDetailsContent(
            cardPairModel = CardPairModel(
                CardModel.Image(
                    "banana_whole",
                    "banana",
                    R.string.banana,
                    R.drawable.img_banana_whole
                ),
                CardModel.Image(
                    "banana_half",
                    "banana",
                    R.string.banana,
                    R.drawable.img_banana_half
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CardPairDetailsContentImagesDifferentTextPreview() {
    AppTheme {
        CardPairDetailsContent(
            cardPairModel = CardPairModel(
                CardModel.Image(
                    "banana_whole",
                    "banana",
                    R.string.banana,
                    R.drawable.img_banana_whole
                ),
                CardModel.Image(
                    "banana_half",
                    "banana",
                    R.string.orange,
                    R.drawable.img_banana_half
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CardPairDetailsContentImageAndTextPreview() {
    AppTheme {
        CardPairDetailsContent(
            cardPairModel = CardPairModel(
                CardModel.Text(
                    "banana_whole",
                    "banana",
                    R.string.banana,
                ),
                CardModel.Image(
                    "banana_half",
                    "banana",
                    R.string.orange,
                    R.drawable.img_banana_whole
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CardPairDetailsContentTextsPreview() {
    AppTheme {
        CardPairDetailsContent(
            cardPairModel = CardPairModel(
                CardModel.Text(
                    "banana_whole",
                    "banana",
                    R.string.banana,
                ),
                CardModel.Text(
                    "banana_half",
                    "banana",
                    R.string.banana,
                )
            )
        )
    }
}
