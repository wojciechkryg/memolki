package com.wojdor.memolki.ui.feature.collection

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel
import androidx.navigation.NavController
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.domain.model.CollectionCardPairModel
import com.wojdor.memolki.ui.ads.RewardedAd
import com.wojdor.memolki.ui.ads.show
import com.wojdor.memolki.ui.app.navigateToCardPairDetailsScreen
import com.wojdor.memolki.ui.app.navigateToEnableNotifications
import com.wojdor.memolki.ui.app.navigateToShop
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.feature.cardpairdetails.CardPairDetailsIntent
import com.wojdor.memolki.ui.feature.cardpairdetails.CardPairDetailsViewModel
import com.wojdor.memolki.ui.feature.collection.component.CollectionContent
import com.wojdor.memolki.ui.feature.enablenotifications.EnableNotificationDestination
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun CollectionScreen(
    viewModel: CollectionViewModel = koinViewModel(),
    cardPairDetailsViewModel: CardPairDetailsViewModel = koinViewModel(),
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
            is CollectionEffect.OpenEnableNotificationsScreen ->
                navController.navigateToEnableNotifications(EnableNotificationDestination.COLLECTION.route)

            is CollectionEffect.OpenCardPairDetailsScreen ->
                openCardPairDetailsScreen(
                    viewModel,
                    cardPairDetailsViewModel,
                    navController,
                    effect.cardPairModel
                )

            is CollectionEffect.ShowAd -> activity?.let { showAd(it, viewModel, effect.rewardedAd) }
        }
    }
}

private fun openCardPairDetailsScreen(
    viewModel: CollectionViewModel,
    cardPairDetailsViewModel: CardPairDetailsViewModel,
    navController: NavController,
    cardPairModel: CardPairModel
) {
    val unlockedCardPairs = viewModel.uiState.value.collectionCardPairs
        .filterIsInstance<CollectionCardPairModel.Unlocked>()
        .map { it.cardPair }
    val initialPage = unlockedCardPairs.indexOf(cardPairModel)
        .takeIf { it != -1 } ?: 0
    cardPairDetailsViewModel.sendIntent(
        CardPairDetailsIntent.OnCardPairDetailsShow(
            unlockedCardPairs,
            initialPage
        )
    )
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

@Preview(showBackground = true)
@Composable
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
                R.string.empty,
                R.drawable.img_test_whole
            ),
            CardModel.Image(
                "banana_half",
                "banana",
                R.string.empty,
                R.drawable.img_test_half
            )
        )
    ),
    CollectionCardPairModel.Unlocked(
        CardPairModel(
            CardModel.Image("apple_whole", "apple", R.string.empty, R.drawable.img_test_whole),
            CardModel.Text("apple_half", "apple", R.string.empty)
        )
    ),
    CollectionCardPairModel.Unlocked(
        CardPairModel(
            CardModel.Text("strawberry_whole", "strawberry", R.string.empty),
            CardModel.Text("strawberry_half", "strawberry", R.string.empty)
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
