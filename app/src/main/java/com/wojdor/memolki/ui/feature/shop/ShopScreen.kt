package com.wojdor.memolki.ui.feature.shop

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.wojdor.memolki.ui.ads.RewardedAd
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.feature.collection.CollectionEffect
import com.wojdor.memolki.ui.feature.shop.component.ShopContent
import com.wojdor.memolki.ui.theme.AppTheme

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
            is CollectionEffect.ShowAd -> activity?.let { showAd(it, viewModel, effect.rewardedAd) }
        }
    }
}

private fun showAd(
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

@Composable
private fun HandleState(
    viewModel: ShopViewModel,
    state: ShopState
) {
    val callbacks = ShopCallbacks(
        onRewardCoinsWithAdClick = { viewModel.sendIntent(ShopIntent.OnRewardCoinsWithAdClick) }
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
            state = ShopState()
        )
    }
}
