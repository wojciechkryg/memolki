package com.wojdor.memolki.ui.feature.shop.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.component.AutoSizeText
import com.wojdor.memolki.ui.component.bounceClickEffect
import com.wojdor.memolki.ui.component.rememberShakeOffset
import com.wojdor.memolki.ui.feature.game.component.CardBorder
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.CardShape
import com.wojdor.memolki.ui.theme.spacingL
import com.wojdor.memolki.ui.theme.spacingS
import com.wojdor.memolki.util.throttleClick

@Composable
fun ShopMenuItem(
    modifier: Modifier = Modifier,
    priceText: String,
    descriptionText: String,
    @DrawableRes leftDrawableRes: Int = NO_DRAWABLE,
    @DrawableRes rightDrawableRes: Int,
    onClick: () -> Unit = {},
    isEnabled: Boolean = true
) {
    var isShaking by remember { mutableStateOf(false) }
    val shakeOffset = rememberShakeOffset(isShaking) { isShaking = false }
    CardBorder(
        modifier = modifier
            .let { if (isEnabled) it.bounceClickEffect() else it }
            .graphicsLayer { translationX = shakeOffset }
            .clip(CardShape)
            .let {
                if (isEnabled) {
                    it.clickable(onClick = throttleClick(onClick = onClick))
                } else {
                    it.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { isShaking = true }
                }
            }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = spacingS),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = spacingS)
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                if (leftDrawableRes != NO_DRAWABLE) {
                    Column(
                        modifier = Modifier.weight(2f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            modifier = Modifier.size(64.dp),
                            alignment = Alignment.Center,
                            painter = painterResource(leftDrawableRes),
                            contentDescription = null
                        )
                        AutoSizeText(
                            text = priceText.uppercase(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    AutoSizeText(
                        text = priceText,
                        modifier = Modifier
                            .weight(2f)
                            .padding(start = spacingS),
                        style = MaterialTheme.typography.headlineLarge,
                        softWrap = false
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(start = spacingL, top = spacingS, bottom = spacingS)
                        .weight(3f),
                    text = descriptionText.lowercase(),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Start
                )
            }
            Image(
                modifier = Modifier.size(96.dp),
                painter = painterResource(rightDrawableRes),
                contentDescription = stringResource(R.string.coins)
            )
        }
    }
}

private const val NO_DRAWABLE = -1

@Preview
@Composable
fun ShopMenuItemShortPreview() {
    AppTheme {
        ShopMenuItem(
            priceText = "0.99 PLN",
            descriptionText = "Buy 500",
            rightDrawableRes = R.drawable.ic_coins_pile_big
        )
    }
}

@Preview
@Composable
fun ShopMenuItemLongPreview() {
    AppTheme {
        ShopMenuItem(
            priceText = "149.99 PLN",
            descriptionText = "Unlock everything that can be unlocked",
            rightDrawableRes = R.drawable.ic_coins_pile_big
        )
    }
}

@Preview
@Composable
fun ShopMenuItemAdsPreview() {
    AppTheme {
        ShopMenuItem(
            leftDrawableRes = R.drawable.ic_ads,
            priceText = "Watch Ad",
            descriptionText = "Obtain 500",
            rightDrawableRes = R.drawable.ic_coins_pile_small
        )
    }
}
