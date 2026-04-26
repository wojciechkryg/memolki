package com.wojdor.memolki.ui.feature.cardpairdetails.component

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
import com.wojdor.memolki.shared.resources.*
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.ui.component.AutoSizeText
import com.wojdor.memolki.ui.component.PREVIEW_IMAGE_HALF
import com.wojdor.memolki.ui.component.PREVIEW_IMAGE_WHOLE
import com.wojdor.memolki.ui.component.PreviewBackground
import com.wojdor.memolki.ui.feature.game.component.FrontCardItem
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.spacingL
import com.wojdor.memolki.ui.theme.spacingS
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

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
private fun TextDetails(modifier: Modifier, textRes: StringResource) {
    AutoSizeText(
        modifier = modifier.padding(spacingS),
        text = stringResource(textRes),
        style = MaterialTheme.typography.displaySmall,
    )
}

@Preview
@Composable
private fun CardPairDetailsContentImagesSameTextPreview() {
    AppTheme {
        PreviewBackground {
            CardPairDetailsContent(
                cardPairModel = CardPairModel(
                    CardModel.Image(
                        "banana_whole",
                        "banana",
                        Res.string.banana,
                        PREVIEW_IMAGE_WHOLE
                    ),
                    CardModel.Image(
                        "banana_half",
                        "banana",
                        Res.string.banana,
                        PREVIEW_IMAGE_HALF
                    )
                )
            )
        }
    }
}

@Preview
@Composable
private fun CardPairDetailsContentImagesDifferentTextPreview() {
    AppTheme {
        PreviewBackground {
            CardPairDetailsContent(
                cardPairModel = CardPairModel(
                    CardModel.Image(
                        "banana_whole",
                        "banana",
                        Res.string.banana,
                        PREVIEW_IMAGE_WHOLE
                    ),
                    CardModel.Image(
                        "banana_half",
                        "banana",
                        Res.string.apple,
                        PREVIEW_IMAGE_HALF
                    )
                )
            )
        }
    }
}

@Preview
@Composable
private fun CardPairDetailsContentImageAndTextPreview() {
    AppTheme {
        PreviewBackground {
            CardPairDetailsContent(
                cardPairModel = CardPairModel(
                    CardModel.Text(
                        "banana_whole",
                        "banana",
                        Res.string.banana,
                    ),
                    CardModel.Image(
                        "banana_half",
                        "banana",
                        Res.string.banana,
                        PREVIEW_IMAGE_WHOLE
                    )
                )
            )
        }
    }
}

@Preview
@Composable
private fun CardPairDetailsContentTextsPreview() {
    AppTheme {
        PreviewBackground {
            CardPairDetailsContent(
                cardPairModel = CardPairModel(
                    CardModel.Text(
                        "banana_whole",
                        "banana",
                        Res.string.banana,
                    ),
                    CardModel.Text(
                        "banana_half",
                        "banana",
                        Res.string.banana,
                    )
                )
            )
        }
    }
}
