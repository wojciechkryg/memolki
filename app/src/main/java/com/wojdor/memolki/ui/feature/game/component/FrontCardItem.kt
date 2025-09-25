package com.wojdor.memolki.ui.feature.game.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.ui.components.AutoSizeText
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.CardShape

@Composable
fun FrontCardItem(
    modifier: Modifier = Modifier,
    card: CardModel,
    onPress: ((isPressed: Boolean) -> Unit)? = null
) {
    CardBorder(
        modifier = modifier
            .clip(CardShape)
            .then(
                if (onPress != null) {
                    Modifier.pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                onPress(true)
                                tryAwaitRelease()
                                onPress(false)
                            }
                        )
                    }
                } else {
                    Modifier
                }
            )
    ) {
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
            .background(color = Color.White)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        AutoSizeText(
            text = stringResource(card.textRes),
            style = MaterialTheme.typography.displayLarge,
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
    AppTheme {
        FrontCardItem(
            Modifier.size(128.dp),
            CardModel.Text(
                id = "banana",
                pairId = "banana",
                textRes = R.string.banana
            ),
            onPress = {}
        )
    }
}

@Preview
@Composable
private fun FrontCardItemImagePreview() {
    AppTheme {
        FrontCardItem(
            Modifier.size(128.dp),
            CardModel.Image(
                id = "banana",
                pairId = "banana",
                textRes = R.string.banana,
                imageRes = R.drawable.img_banana_whole
            ),
            onPress = {}
        )
    }
}
