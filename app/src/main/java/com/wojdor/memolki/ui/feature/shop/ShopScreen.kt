package com.wojdor.memolki.ui.feature.shop

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.billingclient.api.ProductDetails
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.ShopMenuModel
import com.wojdor.memolki.ui.ads.RewardedAd
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.feature.shop.component.ShopContent
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.util.billing.BillingHandler
import com.wojdor.memolki.util.extension.showToast

@Composable
fun ShopScreen(
    viewModel: ShopViewModel = hiltViewModel(),
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
    val activity = LocalActivity.current
    CollectUiEffects(viewModel) { effect ->
        when (effect) {
            is ShopEffect.ShowAd -> activity?.let {
                onWatchAdClick(
                    it,
                    viewModel,
                    effect.rewardedAd
                )
            }

            is ShopEffect.LaunchBilling -> activity?.let {
                launchBillingFlow(
                    it,
                    effect.billingHandler,
                    effect.productDetails
                )
            }

            is ShopEffect.ShowPurchaseFailedError -> activity?.showToast(R.string.shop_purchase_failed_error)
            is ShopEffect.ShowConnectionError -> activity?.showToast(R.string.shop_connection_error)
        }
    }
}

private fun onWatchAdClick(
    activity: Activity,
    viewModel: ShopViewModel,
    rewardedAd: RewardedAd
) {
    rewardedAd.show(
        activity,
        onGrantReward = { viewModel.sendIntent(ShopIntent.OnAdReward) },
        onAdDismiss = { viewModel.sendIntent(ShopIntent.OnAdDismiss(it)) }
    )
}

private fun launchBillingFlow(
    activity: Activity,
    billingHandler: BillingHandler,
    productDetails: ProductDetails
) {
    billingHandler.launchBillingFlow(activity, productDetails)
}

@Composable
private fun HandleState(
    viewModel: ShopViewModel,
    state: ShopState
) {
    val callbacks = ShopCallbacks(
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

@Preview(showBackground = true)
@Composable
private fun ShopScreenPreview() {
    AppTheme {
        ShopScreen(
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

@Preview(showBackground = true)
@Composable
private fun ShopScreenNoAdPreview() {
    AppTheme {
        ShopScreen(
            state = ShopState(
                coins = 1234,
                menu = listOf(
                    ShopMenuModel.WatchAd(false),
                    ShopMenuModel.BuyCoinsSmallAmount,
                    ShopMenuModel.BuyCoinsBigAmount,
                    ShopMenuModel.BuyAllCards
                )
            )
        )
    }
}
