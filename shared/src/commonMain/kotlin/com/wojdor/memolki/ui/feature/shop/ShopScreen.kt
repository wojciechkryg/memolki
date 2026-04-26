package com.wojdor.memolki.ui.feature.shop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.wojdor.memolki.domain.model.ShopMenuModel
import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.shop_connection_error
import com.wojdor.memolki.shared.resources.shop_purchase_failed_error
import com.wojdor.memolki.ui.ads.RewardedAd
import com.wojdor.memolki.ui.app.navigateToEnableNotifications
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.component.PreviewBackground
import com.wojdor.memolki.ui.feature.enablenotifications.EnableNotificationDestination
import com.wojdor.memolki.ui.feature.shop.component.ShopContent
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.util.billing.BillingHandler
import com.wojdor.memolki.util.extension.Toaster
import com.wojdor.memolki.util.gameservices.GameServices
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ShopScreen(
    viewModel: ShopViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()
    HandleEffect(viewModel, navController)
    HandleState(viewModel, state)
}

@Composable
private fun HandleEffect(
    viewModel: ShopViewModel,
    navController: NavController
) {
    val billingHandler = koinInject<BillingHandler>()
    val gameServices = koinInject<GameServices>()
    val toaster = koinInject<Toaster>()
    val purchaseFailedText = stringResource(Res.string.shop_purchase_failed_error)
    val connectionErrorText = stringResource(Res.string.shop_connection_error)
    CollectUiEffects(viewModel) { effect ->
        when (effect) {
            is ShopEffect.OpenEnableNotificationsScreen ->
                navController.navigateToEnableNotifications(EnableNotificationDestination.SHOP.route)

            is ShopEffect.ShowAd -> onWatchAdClick(viewModel, effect.rewardedAd)

            is ShopEffect.LaunchBilling -> billingHandler.launchBillingFlow(effect.product)

            is ShopEffect.ShowPurchaseFailedError -> toaster.show(purchaseFailedText)
            is ShopEffect.ShowConnectionError -> toaster.show(connectionErrorText)
            is ShopEffect.SendTotalCoinsScore -> viewModel.viewModelScope.launch {
                gameServices.submitTotalCoins(effect.totalCoins)
            }
        }
    }
}

private fun onWatchAdClick(
    viewModel: ShopViewModel,
    rewardedAd: RewardedAd
) {
    rewardedAd.show(
        onGrantReward = { viewModel.sendIntent(ShopIntent.OnAdReward) },
        onAdDismiss = { viewModel.sendIntent(ShopIntent.OnAdDismiss(it)) }
    )
}

@Composable
private fun HandleState(
    viewModel: ShopViewModel,
    state: ShopState
) {
    val callbacks = ShopCallbacks(
        onDailyRewardCollectClick = { viewModel.sendIntent(ShopIntent.OnDailyRewardCollectClick) },
        onWatchAdClick = { viewModel.sendIntent(ShopIntent.OnWatchAdClick) },
        onBuyCoinsSmallAmountClick = { viewModel.sendIntent(ShopIntent.OnBuyCoinsSmallAmountClick) },
        onBuyCoinsBigAmountClick = { viewModel.sendIntent(ShopIntent.OnBuyCoinsBigAmountClick) },
        onBuyAllCardsClick = { viewModel.sendIntent(ShopIntent.OnBuyAllCardsClick) }
    )
    ShopScreen(state, callbacks)
}

@Composable
private fun ShopScreen(
    state: ShopState,
    callbacks: ShopCallbacks = ShopCallbacks()
) {
    ShopContent(state, callbacks)
}

@Preview
@Composable
private fun ShopScreenPreview() {
    AppTheme {
        PreviewBackground {
            ShopScreen(
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
}

@Preview
@Composable
private fun ShopScreenNoAdPreview() {
    AppTheme {
        PreviewBackground {
            ShopScreen(
                state = ShopState(
                    coins = 1234,
                    menu = listOf(
                        ShopMenuModel.WatchAd(false, 25),
                        ShopMenuModel.BuyCoinsSmallAmount("$0.99", 500),
                        ShopMenuModel.BuyCoinsBigAmount("$4.99", 3000),
                        ShopMenuModel.BuyAllCards("$14.99")
                    )
                )
            )
        }
    }
}
