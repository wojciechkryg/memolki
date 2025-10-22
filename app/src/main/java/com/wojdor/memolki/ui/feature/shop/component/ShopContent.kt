package com.wojdor.memolki.ui.feature.shop.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.ShopMenuModel
import com.wojdor.memolki.ui.component.CoinsAmount
import com.wojdor.memolki.ui.feature.shop.ShopCallbacks
import com.wojdor.memolki.ui.feature.shop.ShopState
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun ShopContent(
    state: ShopState,
    callbacks: ShopCallbacks = ShopCallbacks()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CoinsAmount(
                modifier = Modifier.weight(1f),
                coins = state.coins,
                animate = state.animateCoins
            )
        }
        AnimatedContent(
            state.menu,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                it.forEach { menuItem ->
                    Spacer(modifier = Modifier.height(16.dp))
                    when (menuItem) {
                        is ShopMenuModel.WatchAd -> ShopMenuItem(
                            descriptionText = if (menuItem.isAdAvailable) {
                                stringResource(R.string.shop_obtain) + " 25"
                            } else {
                                stringResource(R.string.shop_back_later)
                            },
                            leftDrawableRes = R.drawable.ic_ads,
                            rightDrawableRes = R.drawable.ic_coins_pile_small,
                            onClick = callbacks.onWatchAdClick,
                            isEnabled = menuItem.isAdAvailable
                        )

                        ShopMenuModel.BuyCoinsSmallAmount -> ShopMenuItem(
                            priceText = "$0.99",
                            descriptionText = stringResource(R.string.shop_buy) + " 500",
                            rightDrawableRes = R.drawable.ic_coins_pile_big,
                            onClick = callbacks.onBuyCoinsSmallAmountClick
                        )

                        ShopMenuModel.BuyCoinsBigAmount -> ShopMenuItem(
                            priceText = "$4.99",
                            descriptionText = stringResource(R.string.shop_buy) + " 3000",
                            rightDrawableRes = R.drawable.ic_coins_sack,
                            onClick = callbacks.onBuyCoinsBigAmountClick
                        )

                        ShopMenuModel.BuyAllCards -> ShopMenuItem(
                            priceText = "$14.99",
                            descriptionText = stringResource(R.string.shop_unlock_all_cards),
                            rightDrawableRes = R.drawable.ic_cards_stack,
                            onClick = callbacks.onBuyAllCardsClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ShopContentPreview() {
    AppTheme {
        ShopContent(
            state = ShopState(
                coins = 1234,
                menu = listOf(
                    ShopMenuModel.WatchAd(true),
                    ShopMenuModel.BuyCoinsSmallAmount,
                    ShopMenuModel.BuyCoinsBigAmount,
                    ShopMenuModel.BuyAllCards
                )
            )
        )
    }
}
