package com.wojdor.memolki.ui.feature.collection

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.domain.model.CollectionCardPairModel
import com.wojdor.memolki.ui.ads.RewardedAd
import com.wojdor.memolki.ui.app.navigateToCardPairDetailsScreen
import com.wojdor.memolki.ui.app.navigateToShop
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.feature.cardpairdetails.CardPairDetailsIntent
import com.wojdor.memolki.ui.feature.cardpairdetails.CardPairDetailsViewModel
import com.wojdor.memolki.ui.feature.collection.component.CollectionContent
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun CollectionScreen(
    viewModel: CollectionViewModel = hiltViewModel(),
    cardPairDetailsViewModel: CardPairDetailsViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()
    HandleEffect(viewModel, cardPairDetailsViewModel, navController)
    HandleState(viewModel, state)
}

@Composable
private fun HandleEffect(
    viewModel: CollectionViewModel,
    cardPairDetailsViewModel: CardPairDetailsViewModel,
    navController: NavController
) {
    val activity = LocalActivity.current
    CollectUiEffects(viewModel) { effect ->
        when (effect) {
            is CollectionEffect.OpenShopScreen -> navController.navigateToShop()
            is CollectionEffect.OpenCardPairDetailsScreen -> openCardPairDetailsScreen(
                cardPairDetailsViewModel,
                navController,
                effect.cardPairModel
            )

            is CollectionEffect.ShowAd -> activity?.let { showAd(it, viewModel, effect.rewardedAd) }
        }
    }
}

private fun openCardPairDetailsScreen(
    cardPairDetailsViewModel: CardPairDetailsViewModel,
    navController: NavController,
    cardPairModel: CardPairModel
) {
    cardPairDetailsViewModel.sendIntent(CardPairDetailsIntent.OnCardPairDetailsShow(cardPairModel))
    navController.navigateToCardPairDetailsScreen()
}

private fun showAd(
    activity: Activity,
    viewModel: CollectionViewModel,
    rewardedAd: RewardedAd
) {
    rewardedAd.show(
        activity,
        onGrantReward = { viewModel.sendIntent(CollectionIntent.OnAdReward) },
        onAdDismiss = { viewModel.sendIntent(CollectionIntent.OnAdDismiss(it)) }
    )
}

@Composable
private fun HandleState(
    viewModel: CollectionViewModel,
    state: CollectionState
) {
    val callbacks = CollectionCallbacks(
        onShopButtonClick = {
            viewModel.sendIntent(CollectionIntent.OnShopClick)
        },
        onUnlockedCardPairClick = {
            viewModel.sendIntent(CollectionIntent.OnCardPairClick(it))
        },
        onUnlockWithCoinsClick = {
            viewModel.sendIntent(CollectionIntent.OnUnlockWithCoinsClick(it))
        },
        onUnlockWithAdClick = {
            viewModel.sendIntent(CollectionIntent.OnUnlockWithAdClick(it))
        }
    )
    CollectionScreen(state, callbacks)
}

@Composable
fun CollectionScreen(
    state: CollectionState,
    callbacks: CollectionCallbacks = CollectionCallbacks()
) {
    CollectionContent(state, callbacks)
}

@Composable
@Preview(showBackground = true)
private fun CollectionPreview() {
    AppTheme {
        CollectionContent(
            state = getCollectionStateForPreview()
        )
    }
}

fun getCollectionStateForPreview() = CollectionState(
    coins = 1234,
    collectionCardPairs = getCollectionCardPairsForPreview()
)

private fun getCollectionCardPairsForPreview() = listOf(
    CollectionCardPairModel.Unlocked(
        CardPairModel(
            CardModel.Image(
                "banana_whole",
                "banana",
                R.string.banana,
                R.drawable.img_banana_whole
            ),
            CardModel.Image(
                "banana_half",
                "banana",
                R.string.banana,
                R.drawable.img_banana_half
            )
        )
    ),
    CollectionCardPairModel.Unlocked(
        CardPairModel(
            CardModel.Image("apple_whole", "apple", R.string.apple, R.drawable.img_apple_whole),
            CardModel.Text("apple_half", "apple", R.string.banana)
        )
    ),
    CollectionCardPairModel.Unlocked(
        CardPairModel(
            CardModel.Text("strawberry_whole", "strawberry", R.string.strawberry),
            CardModel.Text("strawberry_half", "strawberry", R.string.strawberry)
        )
    ),
    CollectionCardPairModel.LockedToUnlockWithCoins(100),
    CollectionCardPairModel.LockedToUnlockWithAd,
    CollectionCardPairModel.Locked,
    CollectionCardPairModel.Locked,
    CollectionCardPairModel.Locked,
    CollectionCardPairModel.Locked,
    CollectionCardPairModel.Locked
)
