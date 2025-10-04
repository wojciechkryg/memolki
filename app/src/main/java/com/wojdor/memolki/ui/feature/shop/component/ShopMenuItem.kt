package com.wojdor.memolki.ui.feature.shop.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.feature.game.component.CardBorder
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.CardShape
import com.wojdor.memolki.util.throttleClick

@Composable
fun ShopMenuItem(
    modifier: Modifier = Modifier,
    leftText: String = "",
    rightText: String,
    @DrawableRes leftDrawableRes: Int = NO_DRAWABLE,
    @DrawableRes rightDrawableRes: Int,
    onClick: () -> Unit = {},
    isEnabled: Boolean = true
) {
    CardBorder(
        modifier = modifier.clip(CardShape).let {
            if (isEnabled) {
                it.clickable(onClick = throttleClick(onClick = onClick))
            } else {
                it
            }
        }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                if (leftText.isNotEmpty()) {
                    Text(
                        text = leftText.lowercase(),
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.headlineLarge
                    )
                } else if (leftDrawableRes != NO_DRAWABLE) {
                    Image(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 24.dp)
                            .size(64.dp),
                        painter = painterResource(leftDrawableRes),
                        contentDescription = null
                    )
                }
                Text(
                    modifier = modifier
                        .weight(1f)
                        .padding(vertical = 8.dp),
                    text = rightText.lowercase(),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Start
                )
            }
            Image(
                modifier = Modifier.size(96.dp),
                painter = painterResource(rightDrawableRes),
                contentDescription = null
            )
        }
    }
}

private const val NO_DRAWABLE = -1

@Preview
@Composable
fun ShopMenuItemPreview() {
    AppTheme {
        ShopMenuItem(
            leftText = "$0.99",
            rightText = "Buy 500",
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
            rightText = "Obtain 500",
            rightDrawableRes = R.drawable.ic_coins_pile_small
        )
    }
}
