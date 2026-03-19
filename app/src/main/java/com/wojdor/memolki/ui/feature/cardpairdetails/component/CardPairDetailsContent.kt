package com.wojdor.memolki.ui.feature.cardpairdetails.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.ui.component.AutoSizeText
import com.wojdor.memolki.ui.feature.game.component.FrontCardItem
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.spacingL
import com.wojdor.memolki.ui.theme.spacingS

@Composable
fun CardPairDetailsContent(cardPairModel: CardPairModel) {
    val firstCard = cardPairModel.first
    val isFirstCardText = firstCard is CardModel.Text
    val secondCard = cardPairModel.second
    val isSecondCardText = secondCard is CardModel.Text
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacingL),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CardDetails(
            modifier = Modifier.weight(CARD_WEIGHT),
            card = firstCard
        )
        if (!isFirstCardText && !isSecondCardText && firstCard.textRes != secondCard.textRes) {
            TextDetails(modifier = Modifier.weight(TEXT_WEIGHT), textRes = firstCard.textRes)
        } else if (!isFirstCardText && !isSecondCardText) {
            Spacer(modifier = Modifier.height(spacingL))
        }
        CardDetails(
            modifier = Modifier.weight(CARD_WEIGHT),
            card = secondCard
        )
        if (!isFirstCardText && !isSecondCardText) {
            TextDetails(modifier = Modifier.weight(TEXT_WEIGHT), textRes = secondCard.textRes)
        }
    }
}

@Composable
private fun CardDetails(modifier: Modifier, card: CardModel) {
    FrontCardItem(
        modifier = modifier.aspectRatio(1f),
        card = card
    )
}

private const val CARD_WEIGHT = 3f
private const val TEXT_WEIGHT = 1f

@Composable
private fun TextDetails(modifier: Modifier, @StringRes textRes: Int) {
    AutoSizeText(
        modifier = modifier.padding(spacingS),
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
                    R.string.new_game,
                    R.drawable.img_test_whole
                ),
                CardModel.Image(
                    "banana_half",
                    "banana",
                    R.string.new_game,
                    R.drawable.img_test_half
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
                    R.string.menu,
                    R.drawable.img_test_whole
                ),
                CardModel.Image(
                    "banana_half",
                    "banana",
                    R.string.new_game,
                    R.drawable.img_test_half
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
                    R.string.menu,
                ),
                CardModel.Image(
                    "banana_half",
                    "banana",
                    R.string.menu,
                    R.drawable.img_test_whole
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
                    R.string.menu,
                ),
                CardModel.Text(
                    "banana_half",
                    "banana",
                    R.string.new_game,
                )
            )
        )
    }
}
