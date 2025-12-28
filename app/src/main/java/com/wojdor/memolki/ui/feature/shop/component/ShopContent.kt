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
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                it.forEach { menuItem ->
                    Spacer(modifier = Modifier.height(16.dp))
                    when (menuItem) {
                        is ShopMenuModel.WatchAd -> ShopMenuItem(
                            priceText = stringResource(R.string.watch_ad),
                            descriptionText = if (menuItem.isAvailable) {
                                stringResource(R.string.shop_obtain, menuItem.coinsToGrant)
                            } else {
                                stringResource(R.string.shop_back_later)
                            },
                            leftDrawableRes = R.drawable.ic_ads,
                            rightDrawableRes = R.drawable.ic_coins_pile_small,
                            onClick = callbacks.onWatchAdClick,
                            isEnabled = menuItem.isAvailable
                        )

                        is ShopMenuModel.BuyCoinsSmallAmount -> ShopMenuItem(
                            priceText = menuItem.formattedPrice,
                            descriptionText = stringResource(
                                R.string.shop_buy,
                                menuItem.coinsToGrant
                            ),
                            rightDrawableRes = R.drawable.ic_coins_pile_big,
                            onClick = callbacks.onBuyCoinsSmallAmountClick,
                            isEnabled = menuItem.isAvailable
                        )

                        is ShopMenuModel.BuyCoinsBigAmount -> ShopMenuItem(
                            priceText = menuItem.formattedPrice,
                            descriptionText = stringResource(
                                R.string.shop_buy,
                                menuItem.coinsToGrant
                            ),
                            rightDrawableRes = R.drawable.ic_coins_sack,
                            onClick = callbacks.onBuyCoinsBigAmountClick,
                            isEnabled = menuItem.isAvailable
                        )

                        is ShopMenuModel.BuyAllCards -> ShopMenuItem(
                            priceText = menuItem.formattedPrice,
                            descriptionText = stringResource(R.string.shop_unlock_all_cards),
                            rightDrawableRes = R.drawable.ic_cards_stack,
                            onClick = callbacks.onBuyAllCardsClick,
                            isEnabled = menuItem.isAvailable
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
                    ShopMenuModel.WatchAd(true, 25),
                    ShopMenuModel.BuyCoinsSmallAmount("$0.99", 500),
                    ShopMenuModel.BuyCoinsBigAmount("$4.99", 3000),
                    ShopMenuModel.BuyAllCards("$14.99")
                )
            )
        )
    }
}
